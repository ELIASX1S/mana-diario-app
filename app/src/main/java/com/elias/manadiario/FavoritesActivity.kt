package com.elias.manadiario

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)

        carregarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        carregarFavoritos()
    }

    private fun carregarFavoritos() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(this)
        val favoritosSet = preferencias.getStringSet("frases_favoritas", setOf())
        val favoritosList = favoritosSet?.toMutableList() ?: mutableListOf()

        if (favoritosList.isEmpty()) {
            favoritesRecyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            favoritesRecyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
            favoritesRecyclerView.adapter = FavoritesAdapter(favoritosList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}