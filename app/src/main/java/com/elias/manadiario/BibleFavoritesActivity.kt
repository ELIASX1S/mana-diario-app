package com.elias.manadiario

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BibleFavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_favorites)

        val toolbar: Toolbar = findViewById(R.id.bibleFavoritesToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Versículos Favoritos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.bibleFavoritesRecyclerView)
        val emptyTextView: TextView = findViewById(R.id.emptyBibleFavoritesTextView)

        val favorites = BibleFavoritesManager.getFavorites(this).toMutableList()

        if (favorites.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BibleFavoriteAdapter(favorites)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}