package com.elias.manadiario

import android.content.Intent // Importante para abrir o YouTube
import android.net.Uri        // Importante para o link
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(
    private val videoList: List<Video>,
    // Mantemos esta variável aqui para não dar erro no teu Fragmento,
    // mas vamos ignorá-la e usar a nossa lógica nova de abrir o YouTube direto!
    private val onItemClicked: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int = videoList.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.videoTitleTextView)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.videoThumbnailImageView)

        fun bind(video: Video) {
            titleTextView.text = video.title

            // Carrega a imagem da thumbnail (Qualidade Média - mais rápido)
            val thumbnailUrl = "https://img.youtube.com/vi/${video.videoId}/mqdefault.jpg"

            Glide.with(itemView.context)
                .load(thumbnailUrl)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(thumbnailImageView)

            // --- AQUI ESTÁ A SOLUÇÃO MÁGICA ---
            itemView.setOnClickListener {
                // Em vez de tentar abrir um player interno que dá erro,
                // nós mandamos o Android abrir o app oficial do YouTube.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.videoId}"))

                // Isto garante que abre numa nova tarefa, evitando bugs de retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                // Inicia o YouTube (ou navegador se não tiver o app)
                itemView.context.startActivity(intent)
            }
        }
    }
}