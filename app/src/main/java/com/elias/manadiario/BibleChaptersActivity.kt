package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class BibleChaptersActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_BIBLE = "bible_prefs"
        private const val KEY_LAST_VERSE_PREFIX = "last_verse_"
    }

    private lateinit var chaptersRecyclerView: RecyclerView
    private lateinit var adapter: BibleChapterAdapter
    private lateinit var allChapters: List<BibleChapter>

    private var bookName: String = "Livro"
    private var bookKey: String = ""
    private var totalChapters: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_chapters)

        bookName = intent.getStringExtra("BOOK_NAME") ?: "Livro"
        bookKey = intent.getStringExtra("BOOK_KEY") ?: ""
        totalChapters = intent.getIntExtra("BOOK_CHAPTERS", 1)

        val toolbar: Toolbar = findViewById(R.id.bibleChaptersToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = bookName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val subtitleTextView: TextView = findViewById(R.id.bookSubtitleTextView)
        subtitleTextView.text = "Escolha um capítulo para continuar"

        val chapterSearchView: SearchView = findViewById(R.id.chapterSearchView)
        val goToLastReadChapterButton: MaterialButton = findViewById(R.id.goToLastReadChapterButton)

        chaptersRecyclerView = findViewById(R.id.bibleChaptersRecyclerView)
        chaptersRecyclerView.layoutManager = LinearLayoutManager(this)

        allChapters = getChapters(totalChapters)
        val lastReadChapter = findLastReadChapter()

        adapter = BibleChapterAdapter(bookName, bookKey, allChapters, totalChapters, lastReadChapter)
        chaptersRecyclerView.adapter = adapter

        chapterSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val q = newText.orEmpty().trim()
                val filtered = if (q.isBlank()) allChapters else allChapters.filter {
                    it.numero.toString().contains(q)
                }
                adapter = BibleChapterAdapter(bookName, bookKey, filtered, totalChapters, lastReadChapter)
                adapter.updateQuery(q)
                chaptersRecyclerView.adapter = adapter
                return true
            }
        })

        if (lastReadChapter > 0) {
            goToLastReadChapterButton.setOnClickListener { openChapter(lastReadChapter) }
        } else {
            goToLastReadChapterButton.isEnabled = false
        }
    }

    private fun findLastReadChapter(): Int {
        val prefs = getSharedPreferences(PREFS_BIBLE, MODE_PRIVATE)
        for (chapter in totalChapters downTo 1) {
            val key = KEY_LAST_VERSE_PREFIX + "$bookKey:$chapter"
            if (prefs.contains(key)) return chapter
        }
        return -1
    }

    private fun openChapter(chapter: Int) {
        startActivity(Intent(this, BibleVersesActivity::class.java).apply {
            putExtra("BOOK_NAME", bookName)
            putExtra("BOOK_KEY", bookKey)
            putExtra("CHAPTER_NUMBER", chapter)
            putExtra("BOOK_CHAPTERS", totalChapters)
        })
    }

    private fun getChapters(totalChapters: Int): List<BibleChapter> {
        val chapters = mutableListOf<BibleChapter>()
        for (i in 1..totalChapters) chapters.add(BibleChapter(i))
        return chapters
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
