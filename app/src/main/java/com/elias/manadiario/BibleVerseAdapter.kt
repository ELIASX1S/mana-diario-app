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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BibleVerseAdapter(
    private val bookName: String,
    private val chapterNumber: Int,
    private val verses: List<BibleVerse>,
    private val highlightVerseNumber: Int = -1
) : RecyclerView.Adapter<BibleVerseAdapter.BibleVerseViewHolder>() {

    class BibleVerseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView as CardView
        val verseNumberTextView: TextView = itemView.findViewById(R.id.verseNumberTextView)
        val verseTextTextView: TextView = itemView.findViewById(R.id.verseTextTextView)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteVerseButton)
        val shareButton: ImageButton = itemView.findViewById(R.id.shareVerseButton)
        val copyButton: ImageButton = itemView.findViewById(R.id.copyVerseButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleVerseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bible_verse, parent, false)
        return BibleVerseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleVerseViewHolder, position: Int) {
        val verse = verses[position]
        val context = holder.itemView.context

        holder.verseNumberTextView.text = verse.numero.toString()
        holder.verseTextTextView.text = verse.texto

        updateFavoriteIcon(context, holder.favoriteButton, verse.numero)

        if (verse.numero == highlightVerseNumber) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.verse_highlight_background)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.verse_default_background)
            )
        }

        val reference = context.getString(R.string.verse_reference_format, bookName, chapterNumber, verse.numero)
        holder.favoriteButton.contentDescription = context.getString(R.string.cd_favorite_verse, reference)
        holder.shareButton.contentDescription = context.getString(R.string.cd_share_verse, reference)
        holder.copyButton.contentDescription = context.getString(R.string.cd_copy_verse, reference)

        holder.favoriteButton.setOnClickListener {
            val isFavorite = BibleFavoritesManager.isFavorite(
                context,
                bookName,
                chapterNumber,
                verse.numero
            )

            if (isFavorite) {
                BibleFavoritesManager.removeFavorite(
                    context,
                    bookName,
                    chapterNumber,
                    verse.numero
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_verse_removed_from_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                BibleFavoritesManager.addFavorite(
                    context,
                    bookName,
                    chapterNumber,
                    verse.numero,
                    verse.texto
                )
                Toast.makeText(context, context.getString(R.string.toast_verse_favorited), Toast.LENGTH_SHORT).show()
            }

            updateFavoriteIcon(context, holder.favoriteButton, verse.numero)
        }

        holder.copyButton.setOnClickListener {
            copiarVersiculo(context, verse)
        }

        holder.shareButton.setOnClickListener {
            compartilharVersiculo(context, verse)
        }

        holder.itemView.setOnLongClickListener {
            copiarVersiculo(context, verse)
            true
        }
    }

    override fun getItemCount(): Int = verses.size

    private fun updateFavoriteIcon(
        context: Context,
        button: ImageButton,
        verseNumber: Int
    ) {
        val isFavorite = BibleFavoritesManager.isFavorite(
            context,
            bookName,
            chapterNumber,
            verseNumber
        )

        if (isFavorite) {
            button.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            button.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    private fun copiarVersiculo(context: Context, verse: BibleVerse) {
        val textoCompleto = context.getString(
            R.string.verse_text_full_format,
            bookName,
            chapterNumber,
            verse.numero,
            verse.texto
        )
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.clip_label_bible_verse), textoCompleto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.toast_verse_copied), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharVersiculo(context: Context, verse: BibleVerse) {
        val textoCompleto = context.getString(
            R.string.verse_text_full_format,
            bookName,
            chapterNumber,
            verse.numero,
            verse.texto
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textoCompleto)
        }

        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_verse_chooser_short)))
    }
}
