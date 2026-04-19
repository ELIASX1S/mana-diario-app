package com.elias.manadiario

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.google.android.material.chip.Chip
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class BibleBooksActivity : AppCompatActivity() {
    companion object {
        private const val PREFS_BIBLE = "bible_prefs"
        private const val KEY_LAST_BOOK_NAME = "last_book_name"
        private const val KEY_LAST_BOOK_KEY = "last_book_key"
        private const val KEY_LAST_BOOK_CHAPTERS = "last_book_chapters"
        private const val KEY_BIBLE_QUERY = "bible_query"
        private const val KEY_BIBLE_FILTER = "bible_filter"
        private const val KEY_BIBLE_SORT_ALPHA = "bible_sort_alpha"
        private const val KEY_BIBLE_RECENT_KEYS = "bible_recent_keys"
        private const val KEY_BIBLE_ADVANCED_VISIBLE = "bible_advanced_visible"
        private const val MAX_RECENT_BOOKS = 5
        private const val PREFS_BIBLE_NOTES = "bible_notes_prefs"
        private const val KEY_BIBLE_NOTES = "notes"
        private const val LEGACY_NOTES_DELIMITER = "\n\u001F\n"
    }

    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var resultsCountText: TextView
    private lateinit var searchView: SearchView
    private lateinit var toggleAdvancedButton: MaterialButton
    private lateinit var filterChipGroup: ChipGroup
    private lateinit var topControlsRow: View
    private lateinit var continueCard: View
    private lateinit var continueTitle: TextView
    private lateinit var continueSubtitle: TextView
    private lateinit var continueButton: MaterialButton
    private lateinit var recentTitleText: TextView
    private lateinit var recentChipGroup: ChipGroup
    private lateinit var toggleSortButton: MaterialButton
    private lateinit var clearFiltersButton: MaterialButton
    private lateinit var favoritesShortcutButton: MaterialButton
    private lateinit var notesShortcutButton: MaterialButton
    private lateinit var quickActionsRow: View
    private lateinit var adapter: BibleBookAdapter
    private lateinit var allBooks: List<BibleBook>
    private var currentFilter: TestamentFilter = TestamentFilter.ALL
    private var alphabeticalSort = false
    private var suppressFilterCallbacks = false
    private var advancedOptionsVisible = false
    private var hasContinueReading = false
    private var hasRecentBooks = false

    private enum class TestamentFilter { ALL, OLD_TESTAMENT, NEW_TESTAMENT }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_books)

        val toolbar: Toolbar = findViewById(R.id.bibleBooksToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.nav_bible_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchView = findViewById(R.id.bibleSearchView)
        toggleAdvancedButton = findViewById(R.id.toggleBibleAdvancedButton)
        filterChipGroup = findViewById(R.id.bibleFilterChipGroup)
        topControlsRow = findViewById(R.id.bibleTopControlsRow)
        continueCard = findViewById(R.id.continueReadingCard)
        continueTitle = findViewById(R.id.continueReadingTitle)
        continueSubtitle = findViewById(R.id.continueReadingSubtitle)
        continueButton = findViewById(R.id.continueReadingButton)
        recentTitleText = findViewById(R.id.recentBooksTitleText)
        recentChipGroup = findViewById(R.id.recentBooksChipGroup)
        booksRecyclerView = findViewById(R.id.bibleBooksRecyclerView)
        emptyStateText = findViewById(R.id.bibleEmptyStateText)
        resultsCountText = findViewById(R.id.bibleResultsCountText)
        toggleSortButton = findViewById(R.id.toggleSortBibleButton)
        clearFiltersButton = findViewById(R.id.clearBibleFiltersButton)
        favoritesShortcutButton = findViewById(R.id.bibleFavoritesShortcutButton)
        notesShortcutButton = findViewById(R.id.bibleNotesShortcutButton)
        quickActionsRow = findViewById(R.id.bibleQuickActionsRow)
        booksRecyclerView.layoutManager = LinearLayoutManager(this)

        allBooks = getBibleBooks()
        adapter = BibleBookAdapter { book -> saveLastBook(book) }
        booksRecyclerView.adapter = adapter
        adapter.submitList(allBooks)
        updateEmptyState(allBooks)
        updateResultsCount(allBooks.size)
        setupFilters()
        setupContinueReading()
        setupRecentBooks()
        setupActions()
        restoreUiState()
        updateQuickActions()
        updateAdvancedSections()

        // Pré-carrega a Bíblia em segundo plano
        Thread {
            BibleRepository.preload(applicationContext)
        }.start()
    }

    private fun setupContinueReading() {
        val prefs = getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
        val bookName = prefs.getString(KEY_LAST_BOOK_NAME, null)
        val bookKey = prefs.getString(KEY_LAST_BOOK_KEY, null)
        val chapters = prefs.getInt(KEY_LAST_BOOK_CHAPTERS, -1)

        if (bookName.isNullOrBlank() || bookKey.isNullOrBlank() || chapters <= 0) {
            hasContinueReading = false
            updateAdvancedSections()
            return
        }

        hasContinueReading = true
        continueTitle.text = getString(R.string.bible_continue_reading_title)
        continueSubtitle.text = getString(R.string.bible_continue_reading_subtitle, bookName)
        continueButton.setOnClickListener {
            val intent = Intent(this, BibleChaptersActivity::class.java).apply {
                putExtra("BOOK_NAME", bookName)
                putExtra("BOOK_KEY", bookKey)
                putExtra("BOOK_CHAPTERS", chapters)
            }
            startActivity(intent)
        }
        updateAdvancedSections()
    }

    private fun saveLastBook(book: BibleBook) {
        val prefs = getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
        val currentRecentKeys = prefs.getString(KEY_BIBLE_RECENT_KEYS, "")
            .orEmpty()
            .split(",")
            .filter { it.isNotBlank() && it != book.chave }
            .toMutableList()

        currentRecentKeys.add(0, book.chave)
        val trimmedRecent = currentRecentKeys.take(MAX_RECENT_BOOKS)

        prefs.edit()
            .putString(KEY_LAST_BOOK_NAME, book.nome)
            .putString(KEY_LAST_BOOK_KEY, book.chave)
            .putInt(KEY_LAST_BOOK_CHAPTERS, book.capitulos)
            .putString(KEY_BIBLE_RECENT_KEYS, trimmedRecent.joinToString(","))
            .apply()
        setupContinueReading()
        setupRecentBooks()
    }

    private fun setupFilters() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (suppressFilterCallbacks) return true
                applyFilters(newText.orEmpty())
                return true
            }
        })

        filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (suppressFilterCallbacks) return@setOnCheckedStateChangeListener
            currentFilter = when (checkedIds.firstOrNull()) {
                R.id.chipOldTestament -> TestamentFilter.OLD_TESTAMENT
                R.id.chipNewTestament -> TestamentFilter.NEW_TESTAMENT
                else -> TestamentFilter.ALL
            }
            applyFilters(searchView.query?.toString().orEmpty())
        }
    }

    private fun setupActions() {
        toggleSortButton.setOnClickListener {
            alphabeticalSort = !alphabeticalSort
            updateSortButtonAccessibility()
            applyFilters(searchView.query?.toString().orEmpty())
        }

        clearFiltersButton.setOnClickListener {
            filterChipGroup.check(R.id.chipAllBooks)
            searchView.setQuery("", false)
            applyFilters("")
        }

        toggleAdvancedButton.setOnClickListener {
            advancedOptionsVisible = !advancedOptionsVisible
            updateAdvancedSections()
        }

        favoritesShortcutButton.setOnClickListener {
            startActivity(Intent(this, BibleFavoritesActivity::class.java))
        }

        notesShortcutButton.setOnClickListener {
            startActivity(Intent(this, BibleNotesActivity::class.java))
        }
    }

    private fun applyFilters(query: String) {
        val normalizedQuery = query.trim()
        val filteredByTestament = when (currentFilter) {
            TestamentFilter.ALL -> allBooks
            TestamentFilter.OLD_TESTAMENT -> allBooks.take(39)
            TestamentFilter.NEW_TESTAMENT -> allBooks.drop(39)
        }

        var finalList = if (normalizedQuery.isBlank()) {
            filteredByTestament
        } else {
            filteredByTestament.filter { it.nome.contains(normalizedQuery, ignoreCase = true) }
        }

        if (alphabeticalSort) {
            finalList = finalList.sortedBy { it.nome }
        }

        adapter.submitList(finalList)
        updateEmptyState(finalList)
        updateResultsCount(finalList.size)
        updateTopControlsVisibility(normalizedQuery)
    }

    private fun updateEmptyState(books: List<BibleBook>) {
        if (books.isEmpty()) {
            emptyStateText.visibility = View.VISIBLE
        } else {
            emptyStateText.visibility = View.GONE
        }
    }

    private fun updateResultsCount(count: Int) {
        resultsCountText.text = getString(R.string.bible_results_count, count)
    }

    private fun updateTopControlsVisibility(query: String) {
        val hasActiveFilters = query.isNotBlank() ||
            currentFilter != TestamentFilter.ALL ||
            alphabeticalSort

        resultsCountText.visibility = if (hasActiveFilters) View.VISIBLE else View.GONE
        clearFiltersButton.visibility = if (hasActiveFilters) View.VISIBLE else View.GONE
    }

    private fun updateAdvancedSections() {
        filterChipGroup.visibility = if (advancedOptionsVisible) View.VISIBLE else View.GONE
        topControlsRow.visibility = if (advancedOptionsVisible) View.VISIBLE else View.GONE
        continueCard.visibility = if (advancedOptionsVisible && hasContinueReading) View.VISIBLE else View.GONE
        recentChipGroup.visibility = if (advancedOptionsVisible && hasRecentBooks) View.VISIBLE else View.GONE
        recentTitleText.visibility = if (advancedOptionsVisible && hasRecentBooks && recentChipGroup.childCount > 1) View.VISIBLE else View.GONE
        quickActionsRow.visibility = if (advancedOptionsVisible) View.VISIBLE else View.GONE
        toggleAdvancedButton.text = getString(
            if (advancedOptionsVisible) R.string.bible_hide_advanced_options else R.string.bible_show_advanced_options
        )
    }

    private fun updateSortButtonAccessibility() {
        toggleSortButton.contentDescription = getString(
            if (alphabeticalSort) {
                R.string.bible_sort_mode_alphabetical
            } else {
                R.string.bible_sort_mode_canonical
            }
        )
    }

    private fun setupRecentBooks() {
        val prefs = getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
        val recentKeys = prefs.getString(KEY_BIBLE_RECENT_KEYS, "")
            .orEmpty()
            .split(",")
            .filter { it.isNotBlank() }

        if (recentKeys.isEmpty()) {
            hasRecentBooks = false
            recentChipGroup.removeAllViews()
            updateAdvancedSections()
            return
        }

        val booksByKey = allBooks.associateBy { it.chave }
        recentChipGroup.removeAllViews()

        recentKeys.forEach { key ->
            val book = booksByKey[key] ?: return@forEach
            val chip = Chip(this).apply {
                text = book.nome
                isClickable = true
                isCheckable = false
                setOnClickListener { openBookChapters(book) }
            }
            recentChipGroup.addView(chip)
        }

        hasRecentBooks = true
        updateAdvancedSections()
    }

    private fun openBookChapters(book: BibleBook) {
        val intent = Intent(this, BibleChaptersActivity::class.java).apply {
            putExtra("BOOK_NAME", book.nome)
            putExtra("BOOK_KEY", book.chave)
            putExtra("BOOK_CHAPTERS", book.capitulos)
        }
        startActivity(intent)
    }

    private fun restoreUiState() {
        val prefs = getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
        val query = prefs.getString(KEY_BIBLE_QUERY, "").orEmpty()
        alphabeticalSort = prefs.getBoolean(KEY_BIBLE_SORT_ALPHA, false)
        currentFilter = TestamentFilter.valueOf(
            prefs.getString(KEY_BIBLE_FILTER, TestamentFilter.ALL.name) ?: TestamentFilter.ALL.name
        )

        advancedOptionsVisible = prefs.getBoolean(KEY_BIBLE_ADVANCED_VISIBLE, false)
        updateSortButtonAccessibility()

        suppressFilterCallbacks = true
        filterChipGroup.check(
            when (currentFilter) {
                TestamentFilter.OLD_TESTAMENT -> R.id.chipOldTestament
                TestamentFilter.NEW_TESTAMENT -> R.id.chipNewTestament
                TestamentFilter.ALL -> R.id.chipAllBooks
            }
        )
        searchView.setQuery(query, false)
        suppressFilterCallbacks = false

        applyFilters(query)
    }

    override fun onPause() {
        super.onPause()
        getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
            .edit()
            .putString(KEY_BIBLE_QUERY, searchView.query?.toString().orEmpty())
            .putString(KEY_BIBLE_FILTER, currentFilter.name)
            .putBoolean(KEY_BIBLE_SORT_ALPHA, alphabeticalSort)
            .putBoolean(KEY_BIBLE_ADVANCED_VISIBLE, advancedOptionsVisible)
            .apply()
    }

    override fun onResume() {
        super.onResume()
        updateQuickActions()
    }

    private fun updateQuickActions() {
        val totalNotes = getBibleNotesCount()
        notesShortcutButton.text = getString(R.string.bible_shortcut_notes_with_count, totalNotes)
    }

    private fun getBibleNotesCount(): Int {
        val raw = getSharedPreferences(PREFS_BIBLE_NOTES, MODE_PRIVATE)
            .getString(KEY_BIBLE_NOTES, "[]")
            .orEmpty()

        return if (raw.startsWith("[")) {
            val array = JSONArray(raw)
            var validCount = 0
            for (i in 0 until array.length()) {
                val text = array.optJSONObject(i)?.optString("text").orEmpty()
                if (text.isNotBlank()) validCount++
            }
            validCount
        } else {
            raw.split(LEGACY_NOTES_DELIMITER).count { it.isNotBlank() }
        }
    }

    private fun getBibleBooks(): List<BibleBook> {
        return listOf(
            BibleBook("Gênesis", "genesis", 50),
            BibleBook("Êxodo", "exodo", 40),
            BibleBook("Levítico", "levitico", 27),
            BibleBook("Números", "numeros", 36),
            BibleBook("Deuteronômio", "deuteronomio", 34),
            BibleBook("Josué", "josue", 24),
            BibleBook("Juízes", "juizes", 21),
            BibleBook("Rute", "rute", 4),
            BibleBook("1 Samuel", "1samuel", 31),
            BibleBook("2 Samuel", "2samuel", 24),
            BibleBook("1 Reis", "1reis", 22),
            BibleBook("2 Reis", "2reis", 25),
            BibleBook("1 Crônicas", "1cronicas", 29),
            BibleBook("2 Crônicas", "2cronicas", 36),
            BibleBook("Esdras", "esdras", 10),
            BibleBook("Neemias", "neemias", 13),
            BibleBook("Ester", "ester", 10),
            BibleBook("Jó", "jo", 42),
            BibleBook("Salmos", "salmos", 150),
            BibleBook("Provérbios", "proverbios", 31),
            BibleBook("Eclesiastes", "eclesiastes", 12),
            BibleBook("Cânticos", "canticos", 8),
            BibleBook("Isaías", "isaias", 66),
            BibleBook("Jeremias", "jeremias", 52),
            BibleBook("Lamentações", "lamentacoes", 5),
            BibleBook("Ezequiel", "ezequiel", 48),
            BibleBook("Daniel", "daniel", 12),
            BibleBook("Oséias", "oseias", 14),
            BibleBook("Joel", "joel", 3),
            BibleBook("Amós", "amos", 9),
            BibleBook("Obadias", "obadias", 1),
            BibleBook("Jonas", "jonas", 4),
            BibleBook("Miqueias", "miqueias", 7),
            BibleBook("Naum", "naum", 3),
            BibleBook("Habacuque", "habacuque", 3),
            BibleBook("Sofonias", "sofonias", 3),
            BibleBook("Ageu", "ageu", 2),
            BibleBook("Zacarias", "zacarias", 14),
            BibleBook("Malaquias", "malaquias", 4),
            BibleBook("Mateus", "mateus", 28),
            BibleBook("Marcos", "marcos", 16),
            BibleBook("Lucas", "lucas", 24),
            BibleBook("João", "joao", 21),
            BibleBook("Atos", "atos", 28),
            BibleBook("Romanos", "romanos", 16),
            BibleBook("1 Coríntios", "1corintios", 16),
            BibleBook("2 Coríntios", "2corintios", 13),
            BibleBook("Gálatas", "galatas", 6),
            BibleBook("Efésios", "efesios", 6),
            BibleBook("Filipenses", "filipenses", 4),
            BibleBook("Colossenses", "colossenses", 4),
            BibleBook("1 Tessalonicenses", "1tessalonicenses", 5),
            BibleBook("2 Tessalonicenses", "2tessalonicenses", 3),
            BibleBook("1 Timóteo", "1timoteo", 6),
            BibleBook("2 Timóteo", "2timoteo", 4),
            BibleBook("Tito", "tito", 3),
            BibleBook("Filemom", "filemom", 1),
            BibleBook("Hebreus", "hebreus", 13),
            BibleBook("Tiago", "tiago", 5),
            BibleBook("1 Pedro", "1pedro", 5),
            BibleBook("2 Pedro", "2pedro", 3),
            BibleBook("1 João", "1joao", 5),
            BibleBook("2 João", "2joao", 1),
            BibleBook("3 João", "3joao", 1),
            BibleBook("Judas", "judas", 1),
            BibleBook("Apocalipse", "apocalipse", 22)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
