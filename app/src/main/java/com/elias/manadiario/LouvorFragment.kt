package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout

class LouvorFragment : Fragment() {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout (certifica-te que o nome do XML está correto)
        val view = inflater.inflate(R.layout.fragment_louvor, container, false)

        shimmerLayout = view.findViewById(R.id.shimmer_view_container)
        recyclerView = view.findViewById(R.id.videosRecyclerView)

        // 1. Configura o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 2. Simula o carregamento de dados
        carregarDados()

        return view
    }

    private fun carregarDados() {
        // Inicia a animação de carregamento
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Simula um atraso de 2 segundos (como se estivesse a baixar da internet)
        Handler(Looper.getMainLooper()).postDelayed({

            // Pega a lista da nossa fonte de dados
            val listaDeVideos = VideoDataSource.videoList

            // 3. Configura o Adapter com a Ação de Clique
            val adapter = VideoAdapter(listaDeVideos) { video ->
                // --> AQUI ESTÁ A LIGAÇÃO COM O PLAYER <--
                val intent = Intent(activity, PlayerActivity::class.java).apply {
                    putExtra("VIDEO_ID", video.videoId)
                    putExtra("VIDEO_TITLE", video.title) // Enviamos também o título!
                }
                startActivity(intent)
            }

            recyclerView.adapter = adapter

            // Para o shimmer e mostra a lista
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

        }, 2000) // 2000 milissegundos = 2 segundos
    }

    override fun onPause() {
        shimmerLayout.stopShimmer()
        super.onPause()
    }
}