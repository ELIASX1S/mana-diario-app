package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.random.Random

class OracoesFragment : Fragment() {

    private lateinit var chipGroup: ChipGroup
    private lateinit var tvCategoria: TextView
    private lateinit var tvOracao: TextView

    private var categoriaAtual: CategoriaOracao? = null
    private var oracaoAtual: String = ""

    private val categorias = listOf(
        CategoriaOracao(
            nome = "Paz",
            oracoes = listOf(
                "Senhor, acalma meu coração. Tira de mim toda ansiedade, todo medo e toda inquietação. Que a tua paz guarde meus pensamentos e me ajude a descansar em ti. Amém.",
                "Deus, eu entrego este dia nas tuas mãos. Que minha alma encontre descanso na tua presença e que nenhuma preocupação seja maior que a minha fé. Amém.",
                "Pai, quando tudo parecer pesado, lembra-me que eu não estou sozinho. Sustenta-me com tua paz e guia meus passos com serenidade. Amém."
            )
        ),
        CategoriaOracao(
            nome = "Proteção",
            oracoes = listOf(
                "Senhor, guarda minha vida, minha casa e minha família. Livra-nos de todo mal, guia nossos caminhos e cobre-nos com tua presença. Amém.",
                "Deus, coloca tua mão sobre mim hoje. Protege minhas decisões, meus pensamentos e meus passos. Que eu caminhe seguro debaixo da tua graça. Amém.",
                "Pai, cerca minha vida com tua proteção. Que nenhum plano contrário prevaleça contra mim, porque minha confiança está em ti. Amém."
            )
        ),
        CategoriaOracao(
            nome = "Família",
            oracoes = listOf(
                "Senhor, abençoa minha família. Cura feridas, restaura conversas, fortalece os laços e coloca amor onde existe distância. Amém.",
                "Deus, visita minha casa com tua paz. Que haja perdão, união, respeito e presença do Senhor em cada detalhe da nossa família. Amém.",
                "Pai, entrego cada pessoa da minha família em tuas mãos. Cuida do que eu não posso cuidar e guia cada coração para mais perto de ti. Amém."
            )
        ),
        CategoriaOracao(
            nome = "Gratidão",
            oracoes = listOf(
                "Senhor, obrigado por mais um dia. Obrigado pelo cuidado, pelas portas abertas e até pelos processos que estão me ensinando a confiar mais em ti. Amém.",
                "Deus, eu reconheço tua bondade sobre minha vida. Mesmo quando nem tudo é perfeito, eu escolho agradecer porque sei que tu estás comigo. Amém.",
                "Pai, obrigado por me sustentar até aqui. Que meu coração nunca se esqueça das pequenas e grandes bênçãos que vêm de ti. Amém."
            )
        ),
        CategoriaOracao(
            nome = "Força",
            oracoes = listOf(
                "Senhor, renova minhas forças. Quando eu pensar em desistir, lembra-me das tuas promessas e me ajuda a continuar firme. Amém.",
                "Deus, fortalece minha fé nos dias difíceis. Que eu não seja guiado pelo medo, mas pela certeza de que o Senhor está comigo. Amém.",
                "Pai, dá-me coragem para enfrentar este dia. Que minha força venha da tua presença e que meu coração permaneça firme em ti. Amém."
            )
        ),
        CategoriaOracao(
            nome = "Sono",
            oracoes = listOf(
                "Senhor, entrego minha noite em tuas mãos. Acalma minha mente, guarda meu descanso e renova minhas forças enquanto eu durmo. Amém.",
                "Deus, tira de mim o peso deste dia. Que eu durma em paz, sabendo que o Senhor cuida de tudo aquilo que eu não consigo controlar. Amém.",
                "Pai, que minha casa seja cheia de paz nesta noite. Guarda meu sono, meus sonhos e meu coração. Amém."
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_oracoes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById(R.id.chipGroupOracoes)
        tvCategoria = view.findViewById(R.id.tvCategoriaOracao)
        tvOracao = view.findViewById(R.id.tvOracaoTexto)
        val btnNovaOracao = view.findViewById<MaterialButton>(R.id.btnNovaOracao)
        val btnCopiarOracao = view.findViewById<MaterialButton>(R.id.btnCopiarOracao)
        val btnCompartilharOracao = view.findViewById<MaterialButton>(R.id.btnCompartilharOracao)

        montarChipsDeCategorias()
        selecionarCategoria(categorias.first())

        btnNovaOracao.setOnClickListener {
            categoriaAtual?.let { selecionarCategoria(it) }
        }

        btnCopiarOracao.setOnClickListener { copiarOracao() }
        btnCompartilharOracao.setOnClickListener { compartilharOracao() }
    }

    private fun montarChipsDeCategorias() {
        chipGroup.removeAllViews()
        categorias.forEachIndexed { index, categoria ->
            val chip = Chip(requireContext()).apply {
                text = categoria.nome
                isCheckable = true
                id = View.generateViewId()
                setOnClickListener { selecionarCategoria(categoria) }
            }
            chipGroup.addView(chip)
            if (index == 0) chip.isChecked = true
        }
    }

    private fun selecionarCategoria(categoria: CategoriaOracao) {
        categoriaAtual = categoria
        oracaoAtual = categoria.oracoes[Random.nextInt(categoria.oracoes.size)]
        tvCategoria.text = categoria.nome.uppercase()
        tvOracao.text = oracaoAtual
    }

    private fun copiarOracao() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val texto = formatarOracaoParaCompartilhar()
        clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.prayer_room_clip_label), texto))
        Toast.makeText(requireContext(), getString(R.string.prayer_room_copied), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharOracao() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, formatarOracaoParaCompartilhar())
        }
        startActivity(Intent.createChooser(intent, getString(R.string.prayer_room_share_chooser)))
    }

    private fun formatarOracaoParaCompartilhar(): String {
        val categoria = categoriaAtual?.nome ?: getString(R.string.prayer_room_title)
        return "🙏 Oração de $categoria\n\n$oracaoAtual\n\n— Maná Diário"
    }

    private data class CategoriaOracao(
        val nome: String,
        val oracoes: List<String>
    )
}
