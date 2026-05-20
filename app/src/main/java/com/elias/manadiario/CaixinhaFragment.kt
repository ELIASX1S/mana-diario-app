package com.elias.manadiario

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class CaixinhaFragment : Fragment() {

    companion object {
        private const val PREFS_NAME = "promise_box_prefs"
        private const val KEY_HISTORY = "promise_history"
        private const val HISTORY_SEPARATOR = "||"
        private const val MAX_HISTORY_ITEMS = 5
    }

    private lateinit var adViewCaixinha: AdView
    private var promessaAtual: Pair<String, String>? = null

    // Lista de Promessas (Texto e Referência)
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
        Pair("Busquem, pois, em primeiro lugar o Reino de Deus e a sua justiça, e todas essas coisas lhes serão acrescentadas.", "Mateus 6:33")
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
        val btnCompartilharImagem = view.findViewById<Button>(R.id.btnCompartilharImagemPromessa)
        val cardHistorico = view.findViewById<CardView>(R.id.cardHistoricoPromessas)
        val tvHistorico = view.findViewById<TextView>(R.id.tvHistoricoPromessas)

        adViewCaixinha = view.findViewById(R.id.adViewCaixinha)
        configurarBanner()
        atualizarHistoricoNaTela(cardHistorico, tvHistorico)

        // Ao clicar na caixa
        imgCaixa.setOnClickListener {
            // 1. Efeito visual simples (opcional, treme a caixa)
            imgCaixa.animate().rotation(15f).setDuration(100).withEndAction {
                imgCaixa.animate().rotation(-15f).setDuration(100).withEndAction {
                    imgCaixa.animate().rotation(0f).setDuration(100).start()
                }
            }.start()

            // 2. Sorteia uma promessa
            val sorteio = promessas[Random.nextInt(promessas.size)]
            promessaAtual = sorteio
            salvarNoHistorico(sorteio)
            atualizarHistoricoNaTela(cardHistorico, tvHistorico)

            // 3. Preenche o texto
            tvTexto.text = "\"${sorteio.first}\""
            tvRef.text = sorteio.second

            // 4. Mostra o cartão (se estiver escondido)
            if (cardPromessa.visibility == View.GONE) {
                cardPromessa.alpha = 0f
                cardPromessa.visibility = View.VISIBLE
                cardPromessa.animate().alpha(1f).setDuration(500).start()
            }
        }

        // Botão de Partilhar Texto
        btnPartilhar.setOnClickListener {
            val promessa = obterPromessaSelecionada(tvTexto, tvRef) ?: return@setOnClickListener
            val textoParaEnviar = getString(R.string.promise_box_share_text, "\"${promessa.first}\"", promessa.second)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textoParaEnviar)
            startActivity(Intent.createChooser(intent, getString(R.string.promise_box_share_chooser)))
        }

        btnCompartilharImagem.setOnClickListener {
            val promessa = obterPromessaSelecionada(tvTexto, tvRef) ?: return@setOnClickListener
            compartilharImagemDaPromessa(promessa)
        }
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
        val height = 1350
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint().apply {
            shader = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                intArrayOf(Color.rgb(32, 24, 16), Color.rgb(101, 73, 35), Color.rgb(18, 18, 18)),
                floatArrayOf(0f, 0.48f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(48, 255, 214, 128)
        }
        canvas.drawCircle(width / 2f, 270f, 360f, glowPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 236, 190)
            textAlign = Paint.Align.CENTER
            textSize = 54f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText("PROMESSA DE HOJE", width / 2f, 210f, titlePaint)

        val quotePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 58f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
        }
        val quoteLines = quebrarTexto(texto, quotePaint, width - 180)
        val quoteStartY = 430f
        var currentY = quoteStartY
        quoteLines.forEach { line ->
            canvas.drawText(line, width / 2f, currentY, quotePaint)
            currentY += 76f
        }

        val refPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 222, 156)
            textAlign = Paint.Align.CENTER
            textSize = 44f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText(referencia, width / 2f, currentY + 70f, refPaint)

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(220, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            textSize = 34f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
        }
        canvas.drawText("Maná Diário • Uma palavra para sua fé", width / 2f, height - 130f, footerPaint)

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(140, 255, 222, 156)
            strokeWidth = 3f
        }
        canvas.drawLine(260f, height - 205f, width - 260f, height - 205f, linePaint)

        return bitmap
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

    private fun configurarBanner() {
        adViewCaixinha.visibility = View.GONE
        adViewCaixinha.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adViewCaixinha.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                adViewCaixinha.visibility = View.GONE
            }
        }

        val adRequest = AdRequest.Builder().build()
        adViewCaixinha.loadAd(adRequest)
    }

    override fun onDestroyView() {
        if (::adViewCaixinha.isInitialized) {
            adViewCaixinha.destroy()
        }
        super.onDestroyView()
    }
}