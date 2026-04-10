package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TemaAdapter(
    private val lista: List<Tema>,
    private val onClick: (Tema) -> Unit
) : RecyclerView.Adapter<TemaAdapter.TemaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemaViewHolder {
        // Aqui ele "infla" o layout que criaste no Passo 1
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return TemaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemaViewHolder, position: Int) {
        holder.bind(lista[position], onClick)
    }

    override fun getItemCount(): Int = lista.size

    class TemaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNome: TextView = itemView.findViewById(R.id.themeTitle)
        private val imgIcone: ImageView = itemView.findViewById(R.id.themeIcon)

        fun bind(tema: Tema, onClick: (Tema) -> Unit) {
            textNome.text = tema.nome
            // Define o ícone
            imgIcone.setImageResource(tema.iconeResId)

            // Configura o clique
            itemView.setOnClickListener { onClick(tema) }
        }
    }
}

// -- MODELO DE DADOS --
// (Coloquei aqui para facilitar, assim não crias outro arquivo)
data class Tema(
    val id: Int,
    val nome: String,
    val iconeResId: Int
)