package com.elias.manadiario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BibleBookAdapter(
    private val books: List<BibleBook>
) : RecyclerView.Adapter<BibleBookAdapter.BibleBookViewHolder>() {

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
        val book = books[position]

        holder.nomeTextView.text = book.nome
        holder.capitulosTextView.text = "${book.capitulos} capítulos"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BibleChaptersActivity::class.java).apply {
                putExtra("BOOK_NAME", book.nome)
                putExtra("BOOK_KEY", book.chave)
                putExtra("BOOK_CHAPTERS", book.capitulos)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = books.size
}