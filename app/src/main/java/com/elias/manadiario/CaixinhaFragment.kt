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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class CaixinhaFragment : Fragment() {

    companion object {
        private const val PREFS_NAME = "promise_box_prefs"
        private const val KEY_HISTORY = "promise_history"
        private const val KEY_LAST_PROMISE = "last_promise"
        private const val HISTORY_SEPARATOR = "||"
        private const val MAX_HISTORY_ITEMS = 5
    }

    private var promessaAtual: Pair<String, String>? = null

    private val promessas = listOf(
        Pair("O Senhor é o meu pastor, nada me faltará.", "Salmos 23:1"),
        Pair("Tudo posso naquele que me fortalece.", "Filipenses 4:13"),
        Pair("Mil cairão ao teu lado, e dez mil à tua direita, mas não chegará a ti.", "Salmos 91:7"),
        Pair("Porque sou eu que conheço os planos que tenho para vocês, diz o Senhor, planos de fazê-los prosperar e não de causar dano, planos de dar a vocês esperança e um futuro.", "Jeremias 29:11"),
        Pair("Entregue o seu caminho ao Senhor; confie nele, e ele agirá.", "Salmos 37:5"),
        Pair("Não fui eu que ordenei a você? Seja forte e corajoso! Não se apavore nem desanime, pois o Senhor, o seu Deus, estará com você por onde você andar.", "Josué 1:9"),
        Pair("Venham a mim, todos os que estão cansados e sobrecarregados, e eu darei descanso a vocês.", "Mateus 11:28"),
        Pair("Mas os que esperam no Senhor renovarão as forças, subirão com asas como águias; correrão, e não se cansarão; caminharão, e não se fatigarão.", "Isaías 40:31"),
        Pair("Deem graças ao Senhor, porque ele é bom. O seu amor dura para sempre!", "Salmos 136:1"),
        Pair("Busquem, pois, em primeiro lugar o Reino de Deus e a sua justiça, e todas essas coisas lhes serão acrescentadas.", "Mateus 6:33"),
        Pair("Lancem sobre ele toda a sua ansiedade, porque ele tem cuidado de vocês.", "1 Pedro 5:7"),
        Pair("O Senhor lutará por vocês; tão somente acalmem-se.", "Êxodo 14:14"),
        Pair("A minha paz lhes dou; não a dou como o mundo a dá.", "João 14:27"),
        Pair("Deus é o nosso refúgio e fortaleza, socorro bem presente na angústia.", "Salmos 46:1"),
        Pair("Não temas, porque eu sou contigo; não te assombres, porque eu sou o teu Deus.", "Isaías 41:10"),
        Pair("Clama a mim, e responder-te-ei coisas grandes e firmes que não sabes.", "Jeremias 33:3"),
        Pair("O Senhor é a minha luz e a minha salvação; de quem terei medo?", "Salmos 27:1"),
        Pair("Em paz me deito e logo pego no sono, porque só tu, Senhor, me fazes repousar seguro.", "Salmos 4:8"),
        Pair("O choro pode durar uma noite, mas a alegria vem pela manhã.", "Salmos 30:5"),
        Pair("Consagre ao Senhor tudo o que você faz, e os seus planos serão bem-sucedidos.", "Provérbios 16:3"),
        Pair("Aquele que começou boa obra em vocês vai completá-la até o dia de Cristo Jesus.", "Filipenses 1:6"),
        Pair("O meu Deus suprirá todas as necessidades de vocês, de acordo com as suas gloriosas riquezas.", "Filipenses 4:19"),
        Pair("Se Deus é por nós, quem será contra nós?", "Romanos 8:31"),
        Pair("Alegrem-se na esperança, sejam pacientes na tribulação, perseverem na oração.", "Romanos 12:12"),
        Pair("Lâmpada para os meus pés é a tua palavra e luz para o meu caminho.", "Salmos 119:105"),
        Pair("O nome do Senhor é uma torre forte; os justos correm para ela e estão seguros.", "Provérbios 18:10"),
        Pair("Peçam, e será dado; busquem, e encontrarão; batam, e a porta será aberta.", "Mateus 7:7"),
        Pair("Venham a mim, todos os que estão cansados e sobrecarregados, e eu lhes darei descanso.", "Mateus 11:28"),
        Pair("O Senhor te abençoe e te guarde.", "Números 6:24"),
        Pair("Resistam ao diabo, e ele fugirá de vocês.", "Tiago 4:7")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_caixinha, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgCaixa = view.findViewById<ImageView>(R.id.imgCaixa)
        val cardPromessa = view.findViewById<CardView>(R.id.cardPromessa)
        val tvTexto = view.findViewById<TextView>(R.id.tvVersiculoTexto)
        val tvRef = view.findViewById<TextView>(R.id.tvVersiculoRef)
        val btnPartilhar = view.findViewById<Button>(R.id.btnPartilharPromessa)
        val btnCopiar = view.findViewById<Button>(R.id.btnCopiarPromessa)
        val btnCompartilharImagem = view.findViewById<Button>(R.id.btnCompartilharImagemPromessa)
        val cardHistorico = view.findViewById<CardView>(R.id.cardHistoricoPromessas)
        val tvHistorico = view.findViewById<TextView>(R.id.tvHistoricoPromessas)

        atualizarHistoricoNaTela(cardHistorico, tvHistorico)

        imgCaixa.setOnClickListener {
            imgCaixa.animate().rotation(15f).setDuration(100).withEndAction {
                imgCaixa.animate().rotation(-15f).setDuration(100).withEndAction {
                    imgCaixa.animate().rotation(0f).setDuration(100).start()
                }
            }.start()

            val sorteio = sortearPromessaSemRepetir()
            promessaAtual = sorteio
            salvarNoHistorico(sorteio)
            atualizarHistoricoNaTela(cardHistorico, tvHistorico)

            tvTexto.text = "\"${sorteio.first}\""
            tvRef.text = sorteio.second

            if (cardPromessa.visibility == View.GONE) {
                cardPromessa.alpha = 0f
                cardPromessa.visibility = View.VISIBLE
                cardPromessa.animate().alpha(1f).setDuration(500).start()
            }
        }

        btnPartilhar.setOnClickListener {
            val promessa = obterPromessaSelecionada(tvTexto, tvRef) ?: return@setOnClickListener
            val textoParaEnviar = formatarPromessaParaCompartilhar(promessa)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textoParaEnviar)
            startActivity(Intent.createChooser(intent, getString(R.string.promise_box_share_chooser)))
        }

        btnCopiar.setOnClickListener {
            val promessa = obterPromessaSelecionada(tvTexto, tvRef) ?: return@setOnClickListener
            copiarPromessa(promessa)
        }

        btnCompartilharImagem.setOnClickListener {
            val promessa = obterPromessaSelecionada(tvTexto, tvRef) ?: return@setOnClickListener
            compartilharImagemDaPromessa(promessa)
        }
    }

    private fun sortearPromessaSemRepetir(): Pair<String, String> {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ultimaPromessa = prefs.getString(KEY_LAST_PROMISE, null)
        val opcoes = promessas.filter { "${it.first} — ${it.second}" != ultimaPromessa }
        val listaSorteio = if (opcoes.isNotEmpty()) opcoes else promessas
        val sorteio = listaSorteio[Random.nextInt(listaSorteio.size)]

        prefs.edit()
            .putString(KEY_LAST_PROMISE, "${sorteio.first} — ${sorteio.second}")
            .apply()

        return sorteio
    }

    private fun obterPromessaSelecionada(tvTexto: TextView, tvRef: TextView): Pair<String, String>? {
        val promessa = promessaAtual
        if (promessa != null) return promessa

        if (tvTexto.text.isNullOrBlank() || tvRef.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), getString(R.string.promise_box_pick_first), Toast.LENGTH_SHORT).show()
            return null
        }

        return Pair(tvTexto.text.toString().trim().trim('"'), tvRef.text.toString().trim())
    }

    private fun formatarPromessaParaCompartilhar(promessa: Pair<String, String>): String {
        return getString(R.string.promise_box_share_text, "\"${promessa.first}\"", promessa.second)
    }

    private fun copiarPromessa(promessa: Pair<String, String>) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val texto = formatarPromessaParaCompartilhar(promessa)
        val clip = ClipData.newPlainText(getString(R.string.promise_box_clip_label), texto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.promise_box_copied), Toast.LENGTH_SHORT).show()
    }

    private fun salvarNoHistorico(promessa: Pair<String, String>) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val novoItem = "${promessa.first} — ${promessa.second}"
        val historico = obterHistorico().toMutableList()

        historico.remove(novoItem)
        historico.add(0, novoItem)

        prefs.edit()
            .putString(KEY_HISTORY, historico.take(MAX_HISTORY_ITEMS).joinToString(HISTORY_SEPARATOR))
            .apply()
    }

    private fun obterHistorico(): List<String> {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_HISTORY, "")
            .orEmpty()
            .split(HISTORY_SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    private fun atualizarHistoricoNaTela(cardHistorico: CardView, tvHistorico: TextView) {
        val historico = obterHistorico()
        if (historico.isEmpty()) {
            cardHistorico.visibility = View.GONE
            return
        }

        tvHistorico.text = historico.joinToString("\n\n") { "• $it" }
        cardHistorico.visibility = View.VISIBLE
    }

    private fun compartilharImagemDaPromessa(promessa: Pair<String, String>) {
        try {
            val bitmap = criarImagemDaPromessa(promessa.first, promessa.second)
            val imageUri = salvarImagemTemporaria(bitmap)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(intent, getString(R.string.promise_box_image_share_title)))
        } catch (exception: Exception) {
            Toast.makeText(requireContext(), getString(R.string.promise_box_image_share_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarImagemDaPromessa(texto: String, referencia: String): Bitmap {
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
                intArrayOf(Color.rgb(26, 20, 14), Color.rgb(108, 78, 35), Color.rgb(12, 12, 12)),
                floatArrayOf(0f, 0.45f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(42, 255, 218, 140)
        }
        canvas.drawCircle(width / 2f, 410f, 420f, glowPaint)
        canvas.drawCircle(width / 2f, 980f, 620f, glowPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 236, 190)
            textAlign = Paint.Align.CENTER
            textSize = 58f
            letterSpacing = 0.04f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText("PROMESSA DE HOJE", width / 2f, 330f, titlePaint)

        val quotePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calcularTamanhoFonte(texto)
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
        }
        val quoteLines = quebrarTexto(texto, quotePaint, width - 170)
        val lineHeight = quotePaint.textSize + 22f
        val quoteBlockHeight = quoteLines.size * lineHeight
        var currentY = ((height - quoteBlockHeight) / 2f).coerceIn(660f, 920f)

        quoteLines.forEach { line ->
            canvas.drawText(line, width / 2f, currentY, quotePaint)
            currentY += lineHeight
        }

        val refPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 222, 156)
            textAlign = Paint.Align.CENTER
            textSize = 48f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText(referencia, width / 2f, currentY + 75f, refPaint)

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
        canvas.drawText("Maná Diário • Uma palavra para sua fé", width / 2f, height - 185f, footerPaint)

        return bitmap
    }

    private fun calcularTamanhoFonte(texto: String): Float {
        return when {
            texto.length > 170 -> 46f
            texto.length > 130 -> 50f
            texto.length > 90 -> 56f
            else -> 66f
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

        val imageFile = File(imagesDir, "promessa_do_dia.png")
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
    }
}