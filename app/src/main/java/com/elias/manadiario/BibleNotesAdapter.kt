package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BibleNotesAdapter(
    private val onDelete: (BibleNote) -> Unit
) : ListAdapter<BibleNote, BibleNotesAdapter.NoteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteText: TextView = itemView.findViewById(R.id.bibleNoteText)
        private val noteTimestamp: TextView = itemView.findViewById(R.id.bibleNoteTimestamp)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteBibleNoteButton)

        fun bind(note: BibleNote) {
            noteText.text = note.text
            noteTimestamp.text = note.formattedCreatedAt(itemView.context)
            deleteButton.setOnClickListener { onDelete(note) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<BibleNote>() {
        override fun areItemsTheSame(oldItem: BibleNote, newItem: BibleNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BibleNote, newItem: BibleNote): Boolean {
            return oldItem == newItem
        }
    }
}
