package com.elias.manadiario

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class BibleNotesActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_BIBLE_NOTES = "bible_notes_prefs"
        private const val KEY_NOTES = "notes"
    }

    private lateinit var noteInput: EditText
    private lateinit var saveButton: MaterialButton
    private lateinit var emptyStateText: TextView
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var adapter: BibleNotesAdapter

    private val notes = mutableListOf<String>()

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
        notesRecyclerView = findViewById(R.id.bibleNotesRecyclerView)

        adapter = BibleNotesAdapter(
            notes = notes,
            onDelete = { position ->
                notes.removeAt(position)
                adapter.notifyItemRemoved(position)
                persistNotes()
                updateEmptyState()
            }
        )

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = adapter

        loadNotes()

        saveButton.setOnClickListener {
            val text = noteInput.text?.toString()?.trim().orEmpty()
            if (text.isBlank()) return@setOnClickListener

            notes.add(0, text)
            adapter.notifyItemInserted(0)
            notesRecyclerView.scrollToPosition(0)
            noteInput.setText("")
            persistNotes()
            updateEmptyState()
        }
    }

    private fun loadNotes() {
        val raw = getSharedPreferences(PREFS_BIBLE_NOTES, MODE_PRIVATE)
            .getString(KEY_NOTES, "")
            .orEmpty()

        notes.clear()
        if (raw.isNotBlank()) {
            notes.addAll(raw.split("\n\u001F\n").filter { it.isNotBlank() })
        }

        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun persistNotes() {
        getSharedPreferences(PREFS_BIBLE_NOTES, MODE_PRIVATE)
            .edit()
            .putString(KEY_NOTES, notes.joinToString("\n\u001F\n"))
            .apply()
    }

    private fun updateEmptyState() {
        emptyStateText.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
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
