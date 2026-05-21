package com.elias.manadiario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MaisOpcoesAdapter(
    opcoes: List<MaisOpcao>,
    private val onClick: (MaisOpcao) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SECTION = 0
        private const val VIEW_TYPE_OPTION = 1
    }

    private val itens: List<MaisMenuItem> = montarItensAgrupados(opcoes)

    inner class SecaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloSecao: TextView = itemView.findViewById(R.id.textViewSecaoMais)
    }

    inner class MaisOpcaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoOpcao: TextView = itemView.findViewById(R.id.textViewOpcao)
        val descricaoOpcao: TextView = itemView.findViewById(R.id.textViewDescricaoOpcao)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
    }

    override fun getItemViewType(position: Int): Int {
        return when (itens[position]) {
            is MaisMenuItem.Secao -> VIEW_TYPE_SECTION
            is MaisMenuItem.Opcao -> VIEW_TYPE_OPTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SECTION) {
            SecaoViewHolder(inflater.inflate(R.layout.item_mais_secao, parent, false))
        } else {
            MaisOpcaoViewHolder(inflater.inflate(R.layout.item_mais_opcao, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itens[position]) {
            is MaisMenuItem.Secao -> {
                (holder as SecaoViewHolder).tituloSecao.text = item.titulo
            }

            is MaisMenuItem.Opcao -> {
                bindOpcao(holder as MaisOpcaoViewHolder, item.opcao)
            }
        }
    }

    private fun bindOpcao(holder: MaisOpcaoViewHolder, opcao: MaisOpcao) {
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

    override fun getItemCount() = itens.size

    private fun montarItensAgrupados(opcoes: List<MaisOpcao>): List<MaisMenuItem> {
        val porId = opcoes.associateBy { it.id }
        val itens = mutableListOf<MaisMenuItem>()

        fun adicionarSecao(titulo: String, ids: List<String>) {
            val opcoesDaSecao = ids.mapNotNull { porId[it] }
            if (opcoesDaSecao.isEmpty()) return

            itens.add(MaisMenuItem.Secao(titulo))
            opcoesDaSecao.forEach { itens.add(MaisMenuItem.Opcao(it)) }
        }

        adicionarSecao(
            "DEVOCIONAL",
            listOf(
                MaisOpcao.ID_DEVOTIONAL_IMAGE,
                MaisOpcao.ID_GOD_MOMENT,
                MaisOpcao.ID_PRAYERS,
                MaisOpcao.ID_PROMISE_BOX,
                MaisOpcao.ID_FAITH_MESSAGE
            )
        )
        adicionarSecao(
            "BÍBLIA",
            listOf(
                MaisOpcao.ID_SEARCH_BIBLE,
                MaisOpcao.ID_FAVORITE_VERSES,
                MaisOpcao.ID_FAVORITES
            )
        )
        adicionarSecao(
            "APP",
            listOf(
                MaisOpcao.ID_THEMES,
                MaisOpcao.ID_SETTINGS
            )
        )

        val idsUsados = itens.filterIsInstance<MaisMenuItem.Opcao>().map { it.opcao.id }.toSet()
        val extras = opcoes.filterNot { it.id in idsUsados }
        if (extras.isNotEmpty()) {
            itens.add(MaisMenuItem.Secao("OUTROS"))
            extras.forEach { itens.add(MaisMenuItem.Opcao(it)) }
        }

        return itens
    }

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
