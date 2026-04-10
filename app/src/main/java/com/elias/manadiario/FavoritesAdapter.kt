package com.elias.manadiario

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(private val favoritesList: MutableList<String>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    private companion object {
        const val MENU_COPY_ID = 1
        const val MENU_SHARE_ID = 2
        const val MENU_REMOVE_ID = 3
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteTextView: TextView = itemView.findViewById(R.id.favoriteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = favoritesList.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val verse = favoritesList[position]
        holder.favoriteTextView.text = verse

        holder.itemView.setOnLongClickListener { view ->
            val context = view.context
            val currentPosition = holder.adapterPosition

            if (currentPosition == RecyclerView.NO_POSITION) {
                return@setOnLongClickListener true
            }

            mostrarMenu(context, view, currentPosition)
            true
        }
    }

    private fun mostrarMenu(context: Context, anchorView: View, position: Int) {
        val popupMenu = PopupMenu(context, anchorView)

        popupMenu.menu.add(0, MENU_COPY_ID, 0, context.getString(R.string.menu_copy))
        popupMenu.menu.add(0, MENU_SHARE_ID, 1, context.getString(R.string.menu_share))
        popupMenu.menu.add(0, MENU_REMOVE_ID, 2, context.getString(R.string.menu_remove_from_favorites))

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                MENU_COPY_ID -> {
                    copiarTexto(context, favoritesList[position])
                    true
                }
                MENU_SHARE_ID -> {
                    compartilharTexto(context, favoritesList[position])
                    true
                }
                MENU_REMOVE_ID -> {
                    excluirFavorito(context, position)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun copiarTexto(context: Context, texto: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.clip_label_verse), texto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.toast_verse_copied), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharTexto(context: Context, texto: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_verse_message, texto))
        }

        try {
            context.startActivity(
                Intent.createChooser(shareIntent, context.getString(R.string.share_verse_chooser_title))
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.error_no_app_to_share),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun excluirFavorito(context: Context, position: Int) {
        if (position == RecyclerView.NO_POSITION || position >= favoritesList.size) return

        val verso = favoritesList[position]

        val preferencias = PreferenceManager.getDefaultSharedPreferences(context)
        val favoritos = preferencias.getStringSet("frases_favoritas", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()

        favoritos.remove(verso)
        preferencias.edit().putStringSet("frases_favoritas", favoritos).apply()

        favoritesList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favoritesList.size)

        Toast.makeText(context, context.getString(R.string.toast_removed_from_favorites), Toast.LENGTH_SHORT).show()
    }
}
