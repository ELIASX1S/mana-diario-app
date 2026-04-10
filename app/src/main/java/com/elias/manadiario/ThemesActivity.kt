package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ThemesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        // Configura a barra superior (Toolbar)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.themesRecyclerView)

        // 1. A Lista de Temas
        val listaTemas = listOf(
            Tema(1, "Fé", android.R.drawable.ic_menu_view),
            Tema(2, "Esperança", android.R.drawable.ic_menu_compass),
            Tema(3, "Amor", android.R.drawable.btn_star_big_on),
            Tema(4, "Paz", android.R.drawable.ic_menu_gallery),
            Tema(5, "Salvação", android.R.drawable.ic_menu_upload),
            Tema(6, "Sabedoria", android.R.drawable.ic_menu_info_details),
            Tema(7, "Coragem", android.R.drawable.ic_media_play),
            Tema(8, "Perdão", android.R.drawable.ic_menu_revert)
        )

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // 2. Lógica do Clique (AQUI ESTÁ A CORREÇÃO)
        val adapter = TemaAdapter(listaTemas) { tema ->

            // O comando para abrir a nova tela!
            val intent = Intent(this, VersesActivity::class.java)
            intent.putExtra("THEME_NAME", tema.nome)

            // Truque: Vamos mandar sempre 1 agora para testar se a tela abre
            // Depois nós arranjamos os arrays corretos
            intent.putExtra("VERSES_ARRAY_ID", 1)

            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}