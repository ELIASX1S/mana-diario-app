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
        val descricaoOpcao: TextView = itemView.findViewById(R.id.textViewDescricaoOpcao)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaisOpcaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mais_opcao, parent, false)
        return MaisOpcaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaisOpcaoViewHolder, position: Int) {
        val opcao = opcoes[position]
        val context = holder.itemView.context

        holder.textoOpcao.text = opcao.titulo
        holder.descricaoOpcao.text = getDescricaoOpcao(opcao.id)
        holder.imgIcon.setImageResource(opcao.iconRes)
        holder.itemView.setBackgroundResource(R.drawable.bg_menu_item)
        holder.textoOpcao.setTextColor(ContextCompat.getColor(context, R.color.cor_texto_principal))
        holder.descricaoOpcao.setTextColor(ContextCompat.getColor(context, R.color.cor_texto_secundario))
        holder.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.cor_primaria))

        holder.itemView.setOnClickListener {
            onClick(opcao)
        }
    }

    override fun getItemCount() = opcoes.size

    private fun getDescricaoOpcao(id: String): String {
        return when (id) {
            MaisOpcao.ID_DEVOTIONAL_IMAGE -> "Crie uma imagem da palavra de hoje"
            MaisOpcao.ID_GOD_MOMENT -> "Receba palavra, oração e ação prática"
            MaisOpcao.ID_PRAYERS -> "Orações para paz, família, sono e força"
            MaisOpcao.ID_PROMISE_BOX -> "Sorteie e compartilhe promessas bíblicas"
            MaisOpcao.ID_SEARCH_BIBLE -> "Encontre versículos por palavra-chave"
            MaisOpcao.ID_FAVORITE_VERSES -> "Veja seus versículos bíblicos salvos"
            MaisOpcao.ID_FAVORITES -> "Acesse seus devocionais favoritos"
            MaisOpcao.ID_FAITH_MESSAGE -> "Abra uma mensagem especial de fé"
            MaisOpcao.ID_THEMES -> "Explore conteúdos e temas espirituais"
            MaisOpcao.ID_SETTINGS -> "Tema, fonte e notificações do app"
            else -> "Acesso rápido"
        }
    }
}
