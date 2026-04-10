package com.elias.manadiario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BibleSearchAdapter(
    private val results: MutableList<BibleSearchResult>
) : RecyclerView.Adapter<BibleSearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val referenceTextView: TextView = itemView.findViewById(R.id.referenceTextView)
        val verseTextView: TextView = itemView.findViewById(R.id.verseTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val result = results[position]

        holder.referenceTextView.text = "${result.bookName} ${result.chapter}:${result.verse}"
        holder.verseTextView.text = result.text

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, BibleVersesActivity::class.java).apply {
                putExtra("BOOK_NAME", result.bookName)
                putExtra("BOOK_KEY", result.bookKey)
                putExtra("CHAPTER_NUMBER", result.chapter)
                putExtra("HIGHLIGHT_VERSE_NUMBER", result.verse)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<BibleSearchResult>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }
}