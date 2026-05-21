package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.File
import java.io.FileOutputStream
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
        view.findViewById<MaterialButton>(R.id.btnGodMomentShareImage).setOnClickListener { compartilharImagemDoMomento() }

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

    private fun compartilharImagemDoMomento() {
        val momento = momentoAtual ?: return
        val palavra = palavraAtual ?: return

        try {
            val bitmap = criarImagemDoMomento(momento.nome, palavra)
            val imageUri = salvarImagemTemporaria(bitmap)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.god_moment_image_share_title)))
        } catch (exception: Exception) {
            Toast.makeText(requireContext(), getString(R.string.god_moment_image_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarImagemDoMomento(momento: String, palavra: PalavraDoMomento): Bitmap {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint().apply {
            shader = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                intArrayOf(Color.rgb(20, 24, 34), Color.rgb(74, 54, 28), Color.rgb(12, 12, 18)),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(44, 255, 221, 150)
        }
        canvas.drawCircle(width / 2f, 430f, 430f, glowPaint)
        canvas.drawCircle(width / 2f, 1050f, 640f, glowPaint)

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 224, 166)
            textAlign = Paint.Align.CENTER
            textSize = 42f
            letterSpacing = 0.06f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText("MOMENTO COM DEUS", width / 2f, 250f, labelPaint)

        val momentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 72f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText(momento.uppercase(), width / 2f, 350f, momentPaint)

        val messagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calcularFonteImagem(palavra.mensagem)
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
        }
        var currentY = 610f
        quebrarTexto(palavra.mensagem, messagePaint, width - 160).forEach { line ->
            canvas.drawText(line, width / 2f, currentY, messagePaint)
            currentY += messagePaint.textSize + 20f
        }

        val versePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 231, 190)
            textAlign = Paint.Align.CENTER
            textSize = 40f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        currentY += 80f
        quebrarTexto(palavra.versiculo, versePaint, width - 180).forEach { line ->
            canvas.drawText(line, width / 2f, currentY, versePaint)
            currentY += 52f
        }

        val actionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(235, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            textSize = 34f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
        }
        currentY += 90f
        quebrarTexto(palavra.acao, actionPaint, width - 180).take(4).forEach { line ->
            canvas.drawText(line, width / 2f, currentY, actionPaint)
            currentY += 46f
        }

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(150, 255, 222, 156)
            strokeWidth = 4f
        }
        canvas.drawLine(230f, height - 265f, width - 230f, height - 265f, linePaint)

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(230, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            textSize = 38f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
        }
        canvas.drawText("Maná Diário • Palavra, oração e fé", width / 2f, height - 185f, footerPaint)

        return bitmap
    }

    private fun calcularFonteImagem(texto: String): Float {
        return when {
            texto.length > 95 -> 48f
            texto.length > 70 -> 54f
            else -> 62f
        }
    }

    private fun quebrarTexto(texto: String, paint: Paint, maxWidth: Int): List<String> {
        val palavras = texto.split(" ")
        val linhas = mutableListOf<String>()
        var linhaAtual = ""

        palavras.forEach { palavra ->
            val tentativa = if (linhaAtual.isBlank()) palavra else "$linhaAtual $palavra"
            if (paint.measureText(tentativa) <= maxWidth) {
                linhaAtual = tentativa
            } else {
                if (linhaAtual.isNotBlank()) linhas.add(linhaAtual)
                linhaAtual = palavra
            }
        }

        if (linhaAtual.isNotBlank()) linhas.add(linhaAtual)
        return linhas
    }

    private fun salvarImagemTemporaria(bitmap: Bitmap): Uri {
        val imagesDir = File(requireContext().cacheDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()

        val imageFile = File(imagesDir, "momento_com_deus.png")
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
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
