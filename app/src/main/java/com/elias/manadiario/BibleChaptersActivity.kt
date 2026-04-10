package com.elias.manadiario

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BibleChaptersActivity : AppCompatActivity() {

    private lateinit var chaptersRecyclerView: RecyclerView
    private lateinit var adapter: BibleChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_chapters)

        val bookName = intent.getStringExtra("BOOK_NAME") ?: "Livro"
        val bookKey = intent.getStringExtra("BOOK_KEY") ?: ""
        val totalChapters = intent.getIntExtra("BOOK_CHAPTERS", 1)

        val toolbar: Toolbar = findViewById(R.id.bibleChaptersToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = bookName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val subtitleTextView: TextView = findViewById(R.id.bookSubtitleTextView)
        subtitleTextView.text = "Escolha um capítulo para continuar"

        chaptersRecyclerView = findViewById(R.id.bibleChaptersRecyclerView)
        chaptersRecyclerView.layoutManager = LinearLayoutManager(this)

        val chapters = getChapters(totalChapters)
        adapter = BibleChapterAdapter(bookName, bookKey, chapters)
        chaptersRecyclerView.adapter = adapter
    }

    private fun getChapters(totalChapters: Int): List<BibleChapter> {
        val chapters = mutableListOf<BibleChapter>()
        for (i in 1..totalChapters) {
            chapters.add(BibleChapter(i))
        }
        return chapters
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}