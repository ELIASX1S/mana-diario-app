package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.io.File
import java.io.FileOutputStream

class VersesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verses)

        // 1. INICIALIZAR ADS
        MobileAds.initialize(this) {}

        // 2. CARREGAR O BANNER
        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // 3. CONFIGURAR TOOLBAR
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.verses_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 4. PEGAR DADOS DO TEMA
        val themeName = intent.getStringExtra("THEME_NAME") ?: "Versículos"
        val versesArrayId = intent.getIntExtra("VERSES_ARRAY_ID", 0)

        supportActionBar?.title = themeName

        // 5. CARREGAR A LISTA DE VERSÍCULOS
        val versesList = if (versesArrayId != 0) {
            val themeArray = resources.getStringArray(versesArrayId)
            // drop(1) remove o primeiro item se for o título, ajusta se necessário
            themeArray.toList()
        } else {
            emptyList()
        }

        // 6. CONFIGURAR O RECYCLERVIEW
        val recyclerView = findViewById<RecyclerView>(R.id.versesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SimpleVerseAdapter(versesList)
    }
}

// --- ADAPTER COM AS 3 OPÇÕES DE PARTILHA ---
class SimpleVerseAdapter(private val listaVersos: List<String>) :
    RecyclerView.Adapter<SimpleVerseAdapter.VerseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_verso, parent, false)
        return VerseViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val textoVersiculo = listaVersos[position]
        holder.bind(textoVersiculo)

        // AO CLICAR NO VERSÍCULO
        holder.itemView.setOnClickListener {
            mostrarOpcoes(holder.itemView.context, textoVersiculo)
        }
    }

    override fun getItemCount(): Int = listaVersos.size

    private fun mostrarOpcoes(context: Context, texto: String) {
        val opcoes = arrayOf("Partilhar no Zap (Texto)", "Partilhar como Imagem", "Copiar")

        AlertDialog.Builder(context)
            .setTitle("Escolha uma opção")
            .setItems(opcoes) { _, which ->
                when (which) {
                    0 -> partilharTextoComLink(context, texto)
                    1 -> partilharImagem(context, texto)
                    2 -> copiarTexto(context, texto)
                }
            }
            .show()
    }

    // OPÇÃO 1: TEXTO COM LINK (Aparece o Logo no Zap)
    private fun partilharTextoComLink(context: Context, texto: String) {
        val textoFinal = """
            "$texto"
            
            📖 _Maná Diário_
            
            🙏 Receba uma palavra de Deus todos os dias.
            Baixe o app grátis aqui:
            https://play.google.com/store/apps/details?id=com.elias.manadiario
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, textoFinal)
        context.startActivity(Intent.createChooser(intent, "Partilhar Palavra"))
    }

    // OPÇÃO 2: GERAR IMAGEM (Requer layout share_layout.xml)
    private fun partilharImagem(context: Context, texto: String) {
        try {
            val view = LayoutInflater.from(context).inflate(R.layout.share_layout, null)
            val tvTexto = view.findViewById<TextView>(R.id.tvShareVerse)

            if (tvTexto == null) {
                Toast.makeText(context, "Erro: Layout de partilha não encontrado", Toast.LENGTH_SHORT).show()
                return
            }
            tvTexto.text = texto

            // Desenhar
            view.measure(View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            // Salvar
            val imagesFolder = File(context.cacheDir, "images")
            imagesFolder.mkdirs()

            val file = File(imagesFolder, "versiculo_mana.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            // URI (Confirma se está igual ao AndroidManifest!)
            val authority = "com.elias.manadiario.fileprovider"
            val uri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            context.startActivity(Intent.createChooser(intent, "Partilhar Imagem"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao gerar imagem: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // OPÇÃO 3: COPIAR
    private fun copiarTexto(context: Context, texto: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Versículo", texto)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copiado!", Toast.LENGTH_SHORT).show()
    }

    class VerseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textVerso)
        fun bind(texto: String) {
            textView.text = texto
        }
    }
}