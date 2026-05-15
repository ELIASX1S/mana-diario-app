package com.elias.manadiario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BibleChapterAdapter(
    private val bookName: String,
    private val bookKey: String,
    private val chapters: List<BibleChapter>,
    private val totalChapters: Int
) : RecyclerView.Adapter<BibleChapterAdapter.BibleChapterViewHolder>() {

    class BibleChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapterNumberTextView: TextView = itemView.findViewById(R.id.chapterNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_chapter, parent, false)
        return BibleChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.chapterNumberTextView.text = "Capítulo ${chapter.numero}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BibleVersesActivity::class.java).apply {
                putExtra("BOOK_NAME", bookName)
                putExtra("BOOK_KEY", bookKey)
                putExtra("CHAPTER_NUMBER", chapter.numero)
                putExtra("BOOK_CHAPTERS", totalChapters)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = chapters.size
}