package com.elias.manadiario

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BibleVersesActivity : AppCompatActivity() {

    private lateinit var versesRecyclerView: RecyclerView
    private lateinit var subtitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_verses)

        val bookName = intent.getStringExtra("BOOK_NAME") ?: "Livro"
        val bookKey = intent.getStringExtra("BOOK_KEY") ?: ""
        val chapterNumber = intent.getIntExtra("CHAPTER_NUMBER", 1)
        val highlightVerseNumber = intent.getIntExtra("HIGHLIGHT_VERSE_NUMBER", -1)

        val toolbar: Toolbar = findViewById(R.id.bibleVersesToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "$bookName $chapterNumber"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subtitleTextView = findViewById(R.id.chapterSubtitleTextView)
        subtitleTextView.text = "Carregando versículos..."

        versesRecyclerView = findViewById(R.id.bibleVersesRecyclerView)
        versesRecyclerView.layoutManager = LinearLayoutManager(this)

        Thread {
            val verses = BibleRepository.getVerses(applicationContext, bookKey, chapterNumber)

            runOnUiThread {
                versesRecyclerView.adapter = BibleVerseAdapter(
                    bookName = bookName,
                    chapterNumber = chapterNumber,
                    verses = verses,
                    highlightVerseNumber = highlightVerseNumber
                )

                subtitleTextView.text = "Segure um versículo para copiar ou compartilhar"

                if (highlightVerseNumber > 0) {
                    val index = verses.indexOfFirst { it.numero == highlightVerseNumber }
                    if (index >= 0) {
                        versesRecyclerView.post {
                            versesRecyclerView.smoothScrollToPosition(index)
                        }
                    }
                }
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}