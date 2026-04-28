package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MaisOpcoesAdapter(
    private val opcoes: List<MaisOpcao>,
    private val onClick: (MaisOpcao) -> Unit
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
        holder.textoOpcao.text = opcao.titulo
        holder.imgIcon.setImageResource(opcao.iconRes)
        holder.itemView.setBackgroundResource(R.drawable.bg_menu_item)
        holder.textoOpcao.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.cor_texto_principal))
        holder.imgIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.cor_primaria))

        holder.itemView.setOnClickListener {
            onClick(opcao)
        }
    }

    override fun getItemCount() = opcoes.size
}
