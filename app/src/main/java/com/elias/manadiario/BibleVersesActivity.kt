package com.elias.manadiario

import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.progressindicator.LinearProgressIndicator
import androidx.appcompat.widget.SearchView

class BibleVersesActivity : AppCompatActivity() {

    companion object {
        private const val KEY_BIBLE_READ_TEXT_SIZE = "bible_read_text_size"
        private const val KEY_BIBLE_READ_FOCUS_MODE = "bible_read_focus_mode"
        private const val PREFS_BIBLE = "bible_prefs"
        private const val KEY_LAST_VERSE_PREFIX = "last_verse_"
    }

    private lateinit var versesRecyclerView: RecyclerView
    private lateinit var subtitleTextView: TextView
    private lateinit var readingModeChipGroup: ChipGroup
    private lateinit var focusReadingSwitch: SwitchMaterial
    private lateinit var verseAdapter: BibleVerseAdapter
    private lateinit var chapterVerseSearchView: SearchView
    private lateinit var chapterProgressTextView: TextView
    private lateinit var chapterProgressIndicator: LinearProgressIndicator
    private var allVerses: List<BibleVerse> = emptyList()
    private var totalChapters: Int = 1
    private var chapterNumber: Int = 1
    private var bookName: String = "Livro"
    private var bookKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_verses)

        bookName = intent.getStringExtra("BOOK_NAME") ?: "Livro"
        bookKey = intent.getStringExtra("BOOK_KEY") ?: ""
        chapterNumber = intent.getIntExtra("CHAPTER_NUMBER", 1)
        totalChapters = intent.getIntExtra("BOOK_CHAPTERS", 1)
        val highlightVerseNumber = intent.getIntExtra("HIGHLIGHT_VERSE_NUMBER", -1)

        val toolbar: Toolbar = findViewById(R.id.bibleVersesToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "$bookName $chapterNumber"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subtitleTextView = findViewById(R.id.chapterSubtitleTextView)
        val prevChapterButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.prevChapterButton)
        val nextChapterButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.nextChapterButton)
        readingModeChipGroup = findViewById(R.id.readingModeChipGroup)
        chapterVerseSearchView = findViewById(R.id.chapterVerseSearchView)
        chapterProgressTextView = findViewById(R.id.chapterProgressTextView)
        chapterProgressIndicator = findViewById(R.id.chapterProgressIndicator)
        focusReadingSwitch = findViewById(R.id.focusReadingSwitch)
        subtitleTextView.text = "Carregando versículos..."

        versesRecyclerView = findViewById(R.id.bibleVersesRecyclerView)
        versesRecyclerView.layoutManager = LinearLayoutManager(this)
        setupProgressTracker()


        prevChapterButton.isEnabled = chapterNumber > 1
        nextChapterButton.isEnabled = chapterNumber < totalChapters

        prevChapterButton.setOnClickListener {
            if (chapterNumber > 1) {
                openChapter(chapterNumber - 1)
            }
        }

        nextChapterButton.setOnClickListener {
            if (chapterNumber < totalChapters) {
                openChapter(chapterNumber + 1)
            }
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val savedTextSize = prefs.getFloat(KEY_BIBLE_READ_TEXT_SIZE, 16f)
        val savedFocusMode = prefs.getBoolean(KEY_BIBLE_READ_FOCUS_MODE, false)

        Thread {
            val verses = BibleRepository.getVerses(applicationContext, bookKey, chapterNumber)

            runOnUiThread {
                allVerses = verses
                verseAdapter = BibleVerseAdapter(
                    bookName = bookName,
                    chapterNumber = chapterNumber,
                    verses = verses,
                    highlightVerseNumber = highlightVerseNumber,
                    textSizeSp = savedTextSize,
                    focusModeEnabled = savedFocusMode
                )
                versesRecyclerView.adapter = verseAdapter
                updateChapterProgressUi(0)

                subtitleTextView.text = "Segure um versículo para copiar ou compartilhar"
                setupReadingModeControls(savedTextSize, savedFocusMode)
                setupVerseSearch()

                if (highlightVerseNumber > 0) {
                    val index = verses.indexOfFirst { it.numero == highlightVerseNumber }
                    if (index >= 0) {
                        versesRecyclerView.post { versesRecyclerView.smoothScrollToPosition(index) }
                    }
                }

                if (highlightVerseNumber <= 0) {
                    val savedVerse = getSavedVerseForCurrentChapter()
                    if (savedVerse > 0) {
                        val index = verses.indexOfFirst { it.numero == savedVerse }
                        if (index >= 0) {
                            versesRecyclerView.post { versesRecyclerView.scrollToPosition(index) }
                            subtitleTextView.text = "Retomando da leitura anterior (versículo $savedVerse)"
                            updateChapterProgressUi(savedVerse)
                        }
                    }
                }
            }
        }.start()
    }

    private fun setupReadingModeControls(initialTextSize: Float, initialFocusMode: Boolean) {
        when {
            initialTextSize >= 19f -> readingModeChipGroup.check(R.id.chipReadLarge)
            initialTextSize <= 15f -> readingModeChipGroup.check(R.id.chipReadSmall)
            else -> readingModeChipGroup.check(R.id.chipReadMedium)
        }
        focusReadingSwitch.isChecked = initialFocusMode

        readingModeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selected = checkedIds.firstOrNull() ?: R.id.chipReadMedium
            val size = when (selected) {
                R.id.chipReadSmall -> 14f
                R.id.chipReadLarge -> 20f
                else -> 16f
            }
            persistReadingStyle(size, focusReadingSwitch.isChecked)
            verseAdapter.updateReadingStyle(size, focusReadingSwitch.isChecked)
        }

        focusReadingSwitch.setOnCheckedChangeListener { _, isChecked ->
            val currentSize = when (readingModeChipGroup.checkedChipId) {
                R.id.chipReadSmall -> 14f
                R.id.chipReadLarge -> 20f
                else -> 16f
            }
            persistReadingStyle(currentSize, isChecked)
            verseAdapter.updateReadingStyle(currentSize, isChecked)
        }
    }

    private fun setupVerseSearch() {
        chapterVerseSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val q = newText.orEmpty().trim()
                val filtered = if (q.isBlank()) {
                    allVerses
                } else {
                    allVerses.filter { verse ->
                        verse.texto.contains(q, ignoreCase = true) || verse.numero.toString() == q
                    }
                }
                verseAdapter = BibleVerseAdapter(
                    bookName = bookName,
                    chapterNumber = chapterNumber,
                    verses = filtered,
                    highlightVerseNumber = highlightVerseNumber,
                    textSizeSp = PreferenceManager.getDefaultSharedPreferences(this@BibleVersesActivity)
                        .getFloat(KEY_BIBLE_READ_TEXT_SIZE, 16f),
                    focusModeEnabled = focusReadingSwitch.isChecked,
                    searchQuery = q
                )
                versesRecyclerView.adapter = verseAdapter
                return true
            }
        })
    }

    private fun setupProgressTracker() {
        versesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val pos = layoutManager.findFirstVisibleItemPosition()
                if (pos == RecyclerView.NO_POSITION || pos >= allVerses.size) return
                val verseNumber = allVerses[pos].numero
                saveCurrentVerseProgress(verseNumber)
                updateChapterProgressUi(verseNumber)
            }
        })
    }

    private fun updateChapterProgressUi(currentVerse: Int) {
        if (allVerses.isEmpty()) return
        val total = allVerses.size
        val index = allVerses.indexOfFirst { it.numero == currentVerse }.let { if (it < 0) 0 else it + 1 }
        val percent = ((index.toFloat() / total) * 100).toInt().coerceIn(0, 100)
        chapterProgressIndicator.setProgressCompat(percent, true)
        chapterProgressTextView.text = "Progresso: $percent% ($index/$total versículos)"
    }

    private fun saveCurrentVerseProgress(verseNumber: Int) {
        getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
            .edit()
            .putInt(KEY_LAST_VERSE_PREFIX + "$bookKey:$chapterNumber", verseNumber)
            .apply()
    }

    private fun getSavedVerseForCurrentChapter(): Int {
        return getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
            .getInt(KEY_LAST_VERSE_PREFIX + "$bookKey:$chapterNumber", -1)
    }

    private fun openChapter(targetChapter: Int) {
        startActivity(Intent(this, BibleVersesActivity::class.java).apply {
            putExtra("BOOK_NAME", bookName)
            putExtra("BOOK_KEY", bookKey)
            putExtra("CHAPTER_NUMBER", targetChapter)
            putExtra("BOOK_CHAPTERS", totalChapters)
        })
        finish()
    }

    private fun persistReadingStyle(size: Float, focusMode: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putFloat(KEY_BIBLE_READ_TEXT_SIZE, size)
            .putBoolean(KEY_BIBLE_READ_FOCUS_MODE, focusMode)
            .apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
