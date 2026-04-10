package com.elias.manadiario

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class GaleriaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_galeria, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaImagens = listOf(
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card3,
            R.drawable.card4,
            R.drawable.card5,
            R.drawable.card6,
            R.drawable.card7,
            R.drawable.card8,
            R.drawable.card9,
            R.drawable.card10,
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGaleria)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ImagemAdapter(requireContext(), listaImagens)
    }
}

class ImagemAdapter(
    private val context: Context,
    private val imagens: List<Int>
) : RecyclerView.Adapter<ImagemAdapter.ImagemViewHolder>() {

    class ImagemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_imagem, parent, false)
        return ImagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagemViewHolder, position: Int) {
        val imagemId = imagens[position]
        holder.imageView.setImageResource(imagemId)

        holder.itemView.setOnClickListener {
            compartilharImagem(context, holder.imageView)
        }
    }

    override fun getItemCount(): Int = imagens.size

    private fun compartilharImagem(context: Context, imageView: ImageView) {
        try {
            val drawable = imageView.drawable
            if (drawable == null) {
                Toast.makeText(context, "Erro: Imagem não carregou", Toast.LENGTH_SHORT).show()
                return
            }

            val bitmap = (drawable as BitmapDrawable).bitmap

            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            cachePath.listFiles()?.forEach { it.delete() }

            val newFile = File(cachePath, "mensagem_do_dia.jpg")
            FileOutputStream(newFile).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            }

            val authority = "com.elias.manadiario.fileprovider"
            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                authority,
                newFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, "Mensagem do dia via App Maná Diário 🙏")
                type = "image/jpeg"
            }

            context.startActivity(Intent.createChooser(shareIntent, "Partilhar Bênção"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao partilhar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
