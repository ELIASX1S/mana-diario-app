package com.elias.manadiario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BibleBookAdapter(
    private val onBookClick: ((BibleBook) -> Unit)? = null
) : ListAdapter<BibleBook, BibleBookAdapter.BibleBookViewHolder>(DIFF_CALLBACK) {

    class BibleBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.bookNameTextView)
        val capitulosTextView: TextView = itemView.findViewById(R.id.bookChaptersTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleBookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_book, parent, false)
        return BibleBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleBookViewHolder, position: Int) {
        val book = getItem(position)
        val context = holder.itemView.context

        holder.nomeTextView.text = book.nome
        holder.capitulosTextView.text = context.getString(R.string.book_chapters_count, book.capitulos)

        holder.itemView.setOnClickListener {
            onBookClick?.invoke(book)
            val intent = Intent(context, BibleChaptersActivity::class.java).apply {
                putExtra("BOOK_NAME", book.nome)
                putExtra("BOOK_KEY", book.chave)
                putExtra("BOOK_CHAPTERS", book.capitulos)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BibleBook>() {
            override fun areItemsTheSame(oldItem: BibleBook, newItem: BibleBook): Boolean {
                return oldItem.chave == newItem.chave
            }

            override fun areContentsTheSame(oldItem: BibleBook, newItem: BibleBook): Boolean {
                return oldItem == newItem
            }
        }
    }
}
