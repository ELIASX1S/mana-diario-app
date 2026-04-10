package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class BibleFavoriteAdapter(
    private val items: MutableList<FavoriteBibleVerse>
) : RecyclerView.Adapter<BibleFavoriteAdapter.BibleFavoriteViewHolder>() {

    class BibleFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val referenceTextView: TextView = itemView.findViewById(R.id.favoriteReferenceTextView)
        val verseTextView: TextView = itemView.findViewById(R.id.favoriteVerseTextView)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeFavoriteVerseButton)
        val copyButton: ImageButton = itemView.findViewById(R.id.copyFavoriteVerseButton)
        val shareButton: ImageButton = itemView.findViewById(R.id.shareFavoriteVerseButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_favorite, parent, false)
        return BibleFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleFavoriteViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.referenceTextView.text = "${item.bookName} ${item.chapterNumber}:${item.verseNumber}"
        holder.verseTextView.text = item.verseText

        holder.copyButton.setOnClickListener {
            val text = "${item.bookName} ${item.chapterNumber}:${item.verseNumber}\n${item.verseText}"
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("versiculo_favorito", text))
            Toast.makeText(context, "Versículo copiado!", Toast.LENGTH_SHORT).show()
        }

        holder.shareButton.setOnClickListener {
            val text = "${item.bookName} ${item.chapterNumber}:${item.verseNumber}\n${item.verseText}"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Compartilhar versículo"))
        }

        holder.removeButton.setOnClickListener {
            BibleFavoritesManager.removeFavorite(
                context,
                item.bookName,
                item.chapterNumber,
                item.verseNumber
            )
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
            Toast.makeText(context, "Removido dos favoritos.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size
}