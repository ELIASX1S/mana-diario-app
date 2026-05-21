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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        val btnCompartilharImagemOracao = view.findViewById<MaterialButton>(R.id.btnCompartilharImagemOracao)

        montarChipsDeCategorias()
        selecionarCategoria(categorias.first())

        btnNovaOracao.setOnClickListener {
            categoriaAtual?.let { selecionarCategoria(it) }
        }

        btnCopiarOracao.setOnClickListener { copiarOracao() }
        btnCompartilharOracao.setOnClickListener { compartilharOracao() }
        btnCompartilharImagemOracao.setOnClickListener { compartilharImagemDaOracao() }
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

    private fun compartilharImagemDaOracao() {
        val categoria = categoriaAtual?.nome ?: getString(R.string.prayer_room_title)

        try {
            val bitmap = criarImagemDaOracao(categoria, oracaoAtual)
            val imageUri = salvarImagemTemporaria(bitmap)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.prayer_room_image_share_title)))
        } catch (exception: Exception) {
            Toast.makeText(requireContext(), getString(R.string.prayer_room_image_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarImagemDaOracao(categoria: String, oracao: String): Bitmap {
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
                intArrayOf(Color.rgb(18, 22, 32), Color.rgb(82, 58, 30), Color.rgb(10, 10, 14)),
                floatArrayOf(0f, 0.48f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(44, 255, 220, 150)
        }
        canvas.drawCircle(width / 2f, 430f, 440f, glowPaint)
        canvas.drawCircle(width / 2f, 1100f, 620f, glowPaint)

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 224, 166)
            textAlign = Paint.Align.CENTER
            textSize = 44f
            letterSpacing = 0.06f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText("ORAÇÃO DE $categoria".uppercase(), width / 2f, 300f, labelPaint)

        val prayerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calcularFonteOracao(oracao)
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
        }

        val linhas = quebrarTexto(oracao, prayerPaint, width - 170)
        val lineHeight = prayerPaint.textSize + 22f
        val blockHeight = linhas.size * lineHeight
        var currentY = ((height - blockHeight) / 2f).coerceIn(520f, 780f)

        linhas.forEach { line ->
            canvas.drawText(line, width / 2f, currentY, prayerPaint)
            currentY += lineHeight
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
        canvas.drawText("Maná Diário • Ore com fé", width / 2f, height - 185f, footerPaint)

        return bitmap
    }

    private fun calcularFonteOracao(texto: String): Float {
        return when {
            texto.length > 180 -> 42f
            texto.length > 140 -> 46f
            texto.length > 100 -> 52f
            else -> 58f
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

        val imageFile = File(imagesDir, "oracao_mana_diario.png")
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
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
