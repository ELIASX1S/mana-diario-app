package com.elias.manadiario

import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BibleChapterAdapter(
    private val bookName: String,
    private val bookKey: String,
    private val chapters: List<BibleChapter>,
    private val totalChapters: Int,
    private val lastReadChapter: Int = -1
) : RecyclerView.Adapter<BibleChapterAdapter.BibleChapterViewHolder>() {

    private var query: String = ""

    fun updateQuery(newQuery: String) {
        query = newQuery.trim()
        notifyDataSetChanged()
    }

    class BibleChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapterNumberTextView: TextView = itemView.findViewById(R.id.chapterNumberTextView)
        val chapterMetaTextView: TextView = itemView.findViewById(R.id.chapterMetaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_chapter, parent, false)
        return BibleChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        val label = "Capítulo ${chapter.numero}"
        holder.chapterNumberTextView.text = highlight(label, query)
        holder.chapterMetaTextView.text = if (chapter.numero == lastReadChapter) "Última leitura" else "Toque para abrir"

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

    private fun highlight(text: String, query: String): CharSequence {
        if (query.isBlank()) return text
        val idx = text.indexOf(query, ignoreCase = true)
        if (idx < 0) return text
        return SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), idx, idx + query.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
