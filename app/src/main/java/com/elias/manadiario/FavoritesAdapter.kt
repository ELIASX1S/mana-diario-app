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

        popupMenu.menu.add("Copiar")
        popupMenu.menu.add("Compartilhar")
        popupMenu.menu.add("Excluir dos Favoritos")

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Copiar" -> {
                    copiarTexto(context, favoritesList[position])
                    true
                }
                "Compartilhar" -> {
                    compartilharTexto(context, favoritesList[position])
                    true
                }
                "Excluir dos Favoritos" -> {
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
        val clip = ClipData.newPlainText("versiculo", texto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Versículo copiado!", Toast.LENGTH_SHORT).show()
    }

    private fun compartilharTexto(context: Context, texto: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Olha esse versículo:\n\n$texto")
        }

        try {
            context.startActivity(
                Intent.createChooser(shareIntent, "Compartilhar versículo via")
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Nenhum aplicativo encontrado para compartilhar.",
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

        Toast.makeText(context, "Removido dos favoritos!", Toast.LENGTH_SHORT).show()
    }
}