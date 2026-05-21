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
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class DevocionalImagemFragment : Fragment() {

    private lateinit var tvVerse: TextView
    private lateinit var tvReflection: TextView

    private var versiculoAtual: String = ""
    private var reflexaoAtual: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devocional_imagem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvVerse = view.findViewById(R.id.tvDevotionalImageVerse)
        tvReflection = view.findViewById(R.id.tvDevotionalImageReflection)
        val btnShareImage = view.findViewById<MaterialButton>(R.id.btnShareDevotionalImage)
        val btnCopyText = view.findViewById<MaterialButton>(R.id.btnCopyDevotionalImageText)

        carregarDevocionalDoDia()

        btnShareImage.setOnClickListener { compartilharImagemDevocional() }
        btnCopyText.setOnClickListener { copiarDevocional() }
    }

    private fun carregarDevocionalDoDia() {
        val versiculosPrincipais = resources.getStringArray(R.array.versiculos_principais)
        val reflexoes = resources.getStringArray(R.array.reflexoes)
        val indice = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % versiculosPrincipais.size

        versiculoAtual = versiculosPrincipais[indice].trim()
        reflexaoAtual = reflexoes[indice].trim()

        tvVerse.text = versiculoAtual
        tvReflection.text = reflexaoAtual
    }

    private fun copiarDevocional() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                getString(R.string.devotional_image_clip_label),
                formatarDevocional()
            )
        )
        Toast.makeText(requireContext(), getString(R.string.devotional_image_copied), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharImagemDevocional() {
        try {
            val bitmap = criarImagemDevocional()
            val imageUri = salvarImagemTemporaria(bitmap)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.devotional_image_share_title)))
        } catch (exception: Exception) {
            Toast.makeText(requireContext(), getString(R.string.devotional_image_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarImagemDevocional(): Bitmap {
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
                intArrayOf(Color.rgb(20, 24, 34), Color.rgb(88, 62, 30), Color.rgb(10, 10, 14)),
                floatArrayOf(0f, 0.48f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(46, 255, 220, 150)
        }
        canvas.drawCircle(width / 2f, 420f, 430f, glowPaint)
        canvas.drawCircle(width / 2f, 1080f, 660f, glowPaint)

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 224, 166)
            textAlign = Paint.Align.CENTER
            textSize = 44f
            letterSpacing = 0.06f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.BOLD)
        }
        canvas.drawText("PALAVRA DE HOJE", width / 2f, 280f, labelPaint)

        val versePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calcularFonteVersiculo(versiculoAtual)
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
        }

        var currentY = 560f
        quebrarTexto(versiculoAtual, versePaint, width - 170).forEach { line ->
            canvas.drawText(line, width / 2f, currentY, versePaint)
            currentY += versePaint.textSize + 22f
        }

        val reflectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(238, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            textSize = calcularFonteReflexao(reflexaoAtual)
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
        }

        currentY += 85f
        quebrarTexto(reflexaoAtual, reflectionPaint, width - 180).take(8).forEach { line ->
            canvas.drawText(line, width / 2f, currentY, reflectionPaint)
            currentY += reflectionPaint.textSize + 16f
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
        canvas.drawText("Maná Diário • Devocional de hoje", width / 2f, height - 185f, footerPaint)

        return bitmap
    }

    private fun calcularFonteVersiculo(texto: String): Float {
        return when {
            texto.length > 120 -> 46f
            texto.length > 85 -> 52f
            else -> 60f
        }
    }

    private fun calcularFonteReflexao(texto: String): Float {
        return when {
            texto.length > 220 -> 32f
            texto.length > 160 -> 36f
            else -> 40f
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

        val imageFile = File(imagesDir, "devocional_mana_diario.png")
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
    }

    private fun formatarDevocional(): String {
        return "🙏 Palavra de hoje\n\n$versiculoAtual\n\n$reflexaoAtual\n\n— Maná Diário"
    }
}
