package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class BibleNotesActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_BIBLE_NOTES = "bible_notes_prefs"
        private const val KEY_NOTES = "notes"
        private const val FIELD_ID = "id"
        private const val FIELD_TEXT = "text"
        private const val FIELD_CREATED_AT = "createdAt"
        private const val LEGACY_DELIMITER = "\n\u001F\n"
    }

    private lateinit var noteInput: EditText
    private lateinit var saveButton: MaterialButton
    private lateinit var emptyStateText: TextView
    private lateinit var notesCountText: TextView
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var sortChipGroup: ChipGroup
    private lateinit var shareNotesButton: MaterialButton
    private lateinit var clearNotesButton: MaterialButton
    private lateinit var adapter: BibleNotesAdapter

    private val notes = mutableListOf<BibleNote>()
    private var notesQuery: String = ""
    private var sortMode: SortMode = SortMode.RECENT

    private enum class SortMode { RECENT, OLDEST }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_notes)

        val toolbar: Toolbar = findViewById(R.id.bibleNotesToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.bible_notes_title)

        noteInput = findViewById(R.id.bibleNoteInput)
        saveButton = findViewById(R.id.saveBibleNoteButton)
        emptyStateText = findViewById(R.id.bibleNotesEmptyStateText)
        notesCountText = findViewById(R.id.bibleNotesCountText)
        notesRecyclerView = findViewById(R.id.bibleNotesRecyclerView)
        searchView = findViewById(R.id.bibleNotesSearchView)
        sortChipGroup = findViewById(R.id.bibleNotesSortChipGroup)
        shareNotesButton = findViewById(R.id.shareBibleNotesButton)
        clearNotesButton = findViewById(R.id.clearBibleNotesButton)

        adapter = BibleNotesAdapter(onDelete = ::deleteNote)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = adapter

        setupFilters()
        setupTopActions()
        loadNotes()

        saveButton.setOnClickListener {
            val text = noteInput.text?.toString()?.trim().orEmpty()
            if (text.isBlank()) return@setOnClickListener

            val note = BibleNote(
                id = System.currentTimeMillis(),
                text = text,
                createdAt = System.currentTimeMillis()
            )
            notes.add(0, note)
            noteInput.setText("")
            persistNotes()
            renderNotes()
        }
    }

    private fun setupTopActions() {
        shareNotesButton.setOnClickListener {
            if (notes.isEmpty()) {
                Snackbar.make(notesRecyclerView, getString(R.string.bible_notes_nothing_to_share), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exportText = notes
                .sortedByDescending { it.createdAt }
                .joinToString("\n\n") { "• ${it.text}" }

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.bible_notes_share_subject))
                putExtra(Intent.EXTRA_TEXT, exportText)
            }

            startActivity(Intent.createChooser(intent, getString(R.string.bible_notes_share_chooser)))
        }

        clearNotesButton.setOnClickListener {
            if (notes.isEmpty()) return@setOnClickListener

            AlertDialog.Builder(this)
                .setTitle(R.string.bible_notes_clear_title)
                .setMessage(R.string.bible_notes_clear_message)
                .setPositiveButton(R.string.bible_notes_clear_confirm) { _, _ ->
                    val previous = notes.toList()
                    notes.clear()
                    persistNotes()
                    renderNotes()
                    Snackbar.make(notesRecyclerView, getString(R.string.bible_notes_cleared), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.bible_notes_undo)) {
                            notes.addAll(previous)
                            persistNotes()
                            renderNotes()
                        }
                        .show()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    private fun setupFilters() {
        sortChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            sortMode = when (checkedIds.firstOrNull()) {
                R.id.chipNotesOldest -> SortMode.OLDEST
                else -> SortMode.RECENT
            }
            renderNotes()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                notesQuery = newText.orEmpty()
                renderNotes()
                return true
            }
        })
    }

    private fun deleteNote(note: BibleNote) {
        val removedIndex = notes.indexOfFirst { it.id == note.id }
        if (removedIndex == -1) return

        notes.removeAt(removedIndex)
        persistNotes()
        renderNotes()

        Snackbar.make(notesRecyclerView, getString(R.string.bible_notes_deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.bible_notes_undo)) {
                notes.add(removedIndex, note)
                persistNotes()
                renderNotes()
            }
            .show()
    }

    private fun loadNotes() {
        val raw = getSharedPreferences(PREFS_BIBLE_NOTES, MODE_PRIVATE)
            .getString(KEY_NOTES, "[]")
            .orEmpty()

        notes.clear()

        if (raw.startsWith("[")) {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val note = BibleNote(
                    id = item.optLong(FIELD_ID, System.currentTimeMillis() + i),
                    text = item.optString(FIELD_TEXT),
                    createdAt = item.optLong(FIELD_CREATED_AT, System.currentTimeMillis())
                )
                if (note.text.isNotBlank()) notes.add(note)
            }
        } else {
            val migrated = raw.split(LEGACY_DELIMITER)
                .filter { it.isNotBlank() }
                .mapIndexed { index, text ->
                    BibleNote(
                        id = System.currentTimeMillis() + index,
                        text = text,
                        createdAt = System.currentTimeMillis()
                    )
                }
            notes.addAll(migrated)
            persistNotes()
        }

        renderNotes()
    }

    private fun persistNotes() {
        val jsonArray = JSONArray()
        notes.forEach { note ->
            val item = JSONObject()
                .put(FIELD_ID, note.id)
                .put(FIELD_TEXT, note.text)
                .put(FIELD_CREATED_AT, note.createdAt)
            jsonArray.put(item)
        }

        getSharedPreferences(PREFS_BIBLE_NOTES, MODE_PRIVATE)
            .edit()
            .putString(KEY_NOTES, jsonArray.toString())
            .apply()
    }

    private fun renderNotes() {
        var filtered = notes.filter { it.text.contains(notesQuery.trim(), ignoreCase = true) }

        filtered = when (sortMode) {
            SortMode.RECENT -> filtered.sortedByDescending { it.createdAt }
            SortMode.OLDEST -> filtered.sortedBy { it.createdAt }
        }

        adapter.submitList(filtered)
        notesCountText.text = getString(R.string.bible_notes_count, filtered.size)
        emptyStateText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        shareNotesButton.isEnabled = notes.isNotEmpty()
        clearNotesButton.isEnabled = notes.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
