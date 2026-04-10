package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MaisOpcoesAdapter(
    private val opcoes: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<MaisOpcoesAdapter.MaisOpcaoViewHolder>() {

    inner class MaisOpcaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoOpcao: TextView = itemView.findViewById(R.id.textViewOpcao)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaisOpcaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mais_opcao, parent, false)
        return MaisOpcaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaisOpcaoViewHolder, position: Int) {
        val opcao = opcoes[position]
        holder.textoOpcao.text = opcao

        // 🔥 ÍCONES
        when (position) {
            0 -> holder.imgIcon.setImageResource(R.drawable.ic_temas)
            1 -> holder.imgIcon.setImageResource(R.drawable.ic_buscar)
            2 -> holder.imgIcon.setImageResource(R.drawable.ic_favoritos)
            3 -> holder.imgIcon.setImageResource(R.drawable.ic_promessas)
            4 -> holder.imgIcon.setImageResource(R.drawable.ic_favoritos)
            5 -> holder.imgIcon.setImageResource(R.drawable.ic_mensagem)
        }

        holder.itemView.setOnClickListener {
            onClick(opcao)
        }
    }

    override fun getItemCount() = opcoes.size
}