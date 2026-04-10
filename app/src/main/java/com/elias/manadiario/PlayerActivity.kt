package com.elias.manadiario

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlayerActivity : AppCompatActivity() {

    // --> NOVO: Variável para controlar o desaparecimento do título
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val videoId = intent.getStringExtra("VIDEO_ID")
        val videoTitle = intent.getStringExtra("VIDEO_TITLE")

        titleTextView = findViewById(R.id.videoTitlePlayerTextView)
        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        val rootLayout: ConstraintLayout = findViewById(R.id.playerRootLayout) // Adicione um ID ao seu layout principal no XML

        titleTextView.text = videoTitle

        // Inicia o processo para esconder o título após um tempo
        esconderTituloAposDelay()

        // --> NOVO: Adiciona um listener de clique à tela inteira
        // para mostrar o título novamente quando o utilizador tocar
        rootLayout.setOnClickListener {
            mostrarTituloTemporariamente()
        }

        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })
    }

    // --> NOVO: Função para mostrar o título e depois escondê-lo
    private fun mostrarTituloTemporariamente() {
        titleTextView.animate().alpha(1.0f).duration = 200 // Faz o título aparecer suavemente
        titleTextView.visibility = View.VISIBLE
        esconderTituloAposDelay()
    }

    // --> NOVO: Função que agenda o desaparecimento do título
    private fun esconderTituloAposDelay() {
        // Remove quaisquer agendamentos anteriores para evitar múltiplos fades
        handler.removeCallbacksAndMessages(null)
        // Agenda para esconder o título após 3 segundos (3000 milissegundos)
        handler.postDelayed({
            titleTextView.animate().alpha(0.0f).duration = 500 // Faz o título desaparecer suavemente
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // --> NOVO: Limpa o agendamento para evitar memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}