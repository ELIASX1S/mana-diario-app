package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BibleNotesAdapter(
    private val notes: List<String>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<BibleNotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position], position)
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteText: TextView = itemView.findViewById(R.id.bibleNoteText)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteBibleNoteButton)

        fun bind(note: String, position: Int) {
            noteText.text = note
            deleteButton.setOnClickListener { onDelete(position) }
        }
    }
}
