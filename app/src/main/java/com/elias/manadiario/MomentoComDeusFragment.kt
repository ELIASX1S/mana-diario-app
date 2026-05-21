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

class MomentoComDeusFragment : Fragment() {

    private lateinit var chipGroup: ChipGroup
    private lateinit var tvNome: TextView
    private lateinit var tvMensagem: TextView
    private lateinit var tvVersiculo: TextView
    private lateinit var tvOracao: TextView
    private lateinit var tvAcao: TextView

    private var momentoAtual: MomentoEspiritual? = null
    private var palavraAtual: PalavraDoMomento? = null

    private val momentos = listOf(
        MomentoEspiritual(
            nome = "Ansiedade",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "Respire. Deus não perdeu o controle da sua história.",
                    versiculo = "Lancem sobre ele toda a sua ansiedade, porque ele tem cuidado de vocês. — 1 Pedro 5:7",
                    oracao = "Senhor, eu entrego minha ansiedade em tuas mãos. Acalma meus pensamentos e me ajuda a confiar no teu cuidado. Amém.",
                    acao = "Ação prática: pare por um minuto, respire fundo três vezes e diga: Deus está cuidando de mim."
                ),
                PalavraDoMomento(
                    mensagem = "A paz de Deus é maior que a pressão deste momento.",
                    versiculo = "A minha paz lhes dou; não a dou como o mundo a dá. — João 14:27",
                    oracao = "Pai, guarda minha mente e meu coração. Que tua paz ocupe o lugar da preocupação. Amém.",
                    acao = "Ação prática: escreva uma preocupação e ore entregando isso a Deus."
                )
            )
        ),
        MomentoEspiritual(
            nome = "Medo",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "Você não precisa atravessar esse medo sozinho.",
                    versiculo = "Não temas, porque eu sou contigo. — Isaías 41:10",
                    oracao = "Deus, fortalece meu coração. Que minha fé seja maior que o medo que tenta me parar. Amém.",
                    acao = "Ação prática: repita em voz baixa: o Senhor está comigo."
                ),
                PalavraDoMomento(
                    mensagem = "A presença de Deus transforma medo em coragem.",
                    versiculo = "O Senhor é a minha luz e a minha salvação; de quem terei medo? — Salmos 27:1",
                    oracao = "Senhor, ilumina meu caminho e me dá coragem para seguir. Amém.",
                    acao = "Ação prática: dê o próximo passo pequeno, sem tentar resolver tudo hoje."
                )
            )
        ),
        MomentoEspiritual(
            nome = "Desânimo",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "O processo ainda não acabou. Deus ainda está trabalhando.",
                    versiculo = "Aquele que começou boa obra em vocês vai completá-la. — Filipenses 1:6",
                    oracao = "Pai, renova minhas forças e minha esperança. Ajuda-me a continuar mesmo quando eu não vejo tudo claro. Amém.",
                    acao = "Ação prática: escolha uma pequena tarefa importante e conclua hoje."
                ),
                PalavraDoMomento(
                    mensagem = "A sua força pode estar pouca, mas a graça de Deus continua suficiente.",
                    versiculo = "Mas os que esperam no Senhor renovarão as forças. — Isaías 40:31",
                    oracao = "Senhor, levanta minha alma e renova minha fé. Amém.",
                    acao = "Ação prática: ouça uma música de adoração antes de tomar qualquer decisão pesada."
                )
            )
        ),
        MomentoEspiritual(
            nome = "Família",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "Deus também se importa com a sua casa.",
                    versiculo = "Eu e a minha casa serviremos ao Senhor. — Josué 24:15",
                    oracao = "Senhor, abençoa minha família. Traz paz, diálogo, perdão e união para nossa casa. Amém.",
                    acao = "Ação prática: mande uma mensagem de carinho para alguém da sua família hoje."
                ),
                PalavraDoMomento(
                    mensagem = "Onde Deus entra, a paz começa a ser reconstruída.",
                    versiculo = "Como é bom e agradável quando os irmãos convivem em união. — Salmos 133:1",
                    oracao = "Pai, cura feridas familiares e ensina-nos a amar com paciência. Amém.",
                    acao = "Ação prática: ore nominalmente por uma pessoa da sua família."
                )
            )
        ),
        MomentoEspiritual(
            nome = "Noite",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "Descanse. Deus continua cuidando enquanto você dorme.",
                    versiculo = "Em paz me deito e logo pego no sono. — Salmos 4:8",
                    oracao = "Senhor, entrego minha noite a ti. Guarda meu sono, minha mente e minha casa. Amém.",
                    acao = "Ação prática: antes de dormir, agradeça por três coisas simples do dia."
                ),
                PalavraDoMomento(
                    mensagem = "Você pode encerrar este dia em paz.",
                    versiculo = "O Senhor dá força ao seu povo; o Senhor dá ao seu povo a bênção da paz. — Salmos 29:11",
                    oracao = "Deus, tira o peso do meu coração e me dá uma noite tranquila. Amém.",
                    acao = "Ação prática: coloque o celular de lado por alguns minutos e ore em silêncio."
                )
            )
        ),
        MomentoEspiritual(
            nome = "Gratidão",
            palavras = listOf(
                PalavraDoMomento(
                    mensagem = "A gratidão abre os olhos para o cuidado de Deus.",
                    versiculo = "Deem graças ao Senhor, porque ele é bom. — Salmos 136:1",
                    oracao = "Senhor, obrigado por tua bondade. Ajuda-me a perceber tuas bênçãos até nos detalhes pequenos. Amém.",
                    acao = "Ação prática: escreva três motivos de gratidão de hoje."
                ),
                PalavraDoMomento(
                    mensagem = "Mesmo nos dias difíceis, ainda existe motivo para agradecer.",
                    versiculo = "Dêem graças em todas as circunstâncias. — 1 Tessalonicenses 5:18",
                    oracao = "Pai, ensina meu coração a agradecer sem depender de tudo estar perfeito. Amém.",
                    acao = "Ação prática: compartilhe uma palavra de gratidão com alguém."
                )
            )
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_momento_com_deus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById(R.id.chipGroupGodMoment)
        tvNome = view.findViewById(R.id.tvGodMomentName)
        tvMensagem = view.findViewById(R.id.tvGodMomentMessage)
        tvVersiculo = view.findViewById(R.id.tvGodMomentVerse)
        tvOracao = view.findViewById(R.id.tvGodMomentPrayer)
        tvAcao = view.findViewById(R.id.tvGodMomentAction)

        view.findViewById<MaterialButton>(R.id.btnGodMomentNew).setOnClickListener {
            momentoAtual?.let { selecionarMomento(it) }
        }
        view.findViewById<MaterialButton>(R.id.btnGodMomentCopy).setOnClickListener { copiarMomento() }
        view.findViewById<MaterialButton>(R.id.btnGodMomentShare).setOnClickListener { compartilharMomento() }

        montarChips()
        selecionarMomento(momentos.first())
    }

    private fun montarChips() {
        chipGroup.removeAllViews()
        momentos.forEachIndexed { index, momento ->
            val chip = Chip(requireContext()).apply {
                text = momento.nome
                isCheckable = true
                id = View.generateViewId()
                setOnClickListener { selecionarMomento(momento) }
            }
            chipGroup.addView(chip)
            if (index == 0) chip.isChecked = true
        }
    }

    private fun selecionarMomento(momento: MomentoEspiritual) {
        momentoAtual = momento
        palavraAtual = momento.palavras[Random.nextInt(momento.palavras.size)]
        val palavra = palavraAtual ?: return

        tvNome.text = momento.nome.uppercase()
        tvMensagem.text = palavra.mensagem
        tvVersiculo.text = palavra.versiculo
        tvOracao.text = palavra.oracao
        tvAcao.text = palavra.acao
    }

    private fun copiarMomento() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.god_moment_clip_label), formatarMomento()))
        Toast.makeText(requireContext(), getString(R.string.god_moment_copied), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharMomento() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, formatarMomento())
        }
        startActivity(Intent.createChooser(intent, getString(R.string.god_moment_share_chooser)))
    }

    private fun formatarMomento(): String {
        val momento = momentoAtual?.nome.orEmpty()
        val palavra = palavraAtual ?: return ""
        return "🙏 Momento com Deus: $momento\n\n${palavra.mensagem}\n\n${palavra.versiculo}\n\n${palavra.oracao}\n\n${palavra.acao}\n\n— Maná Diário"
    }

    private data class MomentoEspiritual(
        val nome: String,
        val palavras: List<PalavraDoMomento>
    )

    private data class PalavraDoMomento(
        val mensagem: String,
        val versiculo: String,
        val oracao: String,
        val acao: String
    )
}
