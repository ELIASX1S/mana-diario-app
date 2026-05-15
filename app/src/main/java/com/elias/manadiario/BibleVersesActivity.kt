package com.elias.manadiario

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial

class BibleVersesActivity : AppCompatActivity() {

    companion object {
        private const val KEY_BIBLE_READ_TEXT_SIZE = "bible_read_text_size"
        private const val KEY_BIBLE_READ_FOCUS_MODE = "bible_read_focus_mode"
    }

    private lateinit var versesRecyclerView: RecyclerView
    private lateinit var subtitleTextView: TextView
    private lateinit var readingModeChipGroup: ChipGroup
    private lateinit var focusReadingSwitch: SwitchMaterial
    private lateinit var verseAdapter: BibleVerseAdapter

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
        readingModeChipGroup = findViewById(R.id.readingModeChipGroup)
        focusReadingSwitch = findViewById(R.id.focusReadingSwitch)
        subtitleTextView.text = "Carregando versículos..."

        versesRecyclerView = findViewById(R.id.bibleVersesRecyclerView)
        versesRecyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val savedTextSize = prefs.getFloat(KEY_BIBLE_READ_TEXT_SIZE, 16f)
        val savedFocusMode = prefs.getBoolean(KEY_BIBLE_READ_FOCUS_MODE, false)

        Thread {
            val verses = BibleRepository.getVerses(applicationContext, bookKey, chapterNumber)

            runOnUiThread {
                verseAdapter = BibleVerseAdapter(
                    bookName = bookName,
                    chapterNumber = chapterNumber,
                    verses = verses,
                    highlightVerseNumber = highlightVerseNumber,
                    textSizeSp = savedTextSize,
                    focusModeEnabled = savedFocusMode
                )
                versesRecyclerView.adapter = verseAdapter

                subtitleTextView.text = "Segure um versículo para copiar ou compartilhar"
                setupReadingModeControls(savedTextSize, savedFocusMode)

                if (highlightVerseNumber > 0) {
                    val index = verses.indexOfFirst { it.numero == highlightVerseNumber }
                    if (index >= 0) {
                        versesRecyclerView.post { versesRecyclerView.smoothScrollToPosition(index) }
                    }
                }
            }
        }.start()
    }

    private fun setupReadingModeControls(initialTextSize: Float, initialFocusMode: Boolean) {
        when {
            initialTextSize >= 19f -> readingModeChipGroup.check(R.id.chipReadLarge)
            initialTextSize <= 15f -> readingModeChipGroup.check(R.id.chipReadSmall)
            else -> readingModeChipGroup.check(R.id.chipReadMedium)
        }
        focusReadingSwitch.isChecked = initialFocusMode

        readingModeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selected = checkedIds.firstOrNull() ?: R.id.chipReadMedium
            val size = when (selected) {
                R.id.chipReadSmall -> 14f
                R.id.chipReadLarge -> 20f
                else -> 16f
            }
            persistReadingStyle(size, focusReadingSwitch.isChecked)
            verseAdapter.updateReadingStyle(size, focusReadingSwitch.isChecked)
        }

        focusReadingSwitch.setOnCheckedChangeListener { _, isChecked ->
            val currentSize = when (readingModeChipGroup.checkedChipId) {
                R.id.chipReadSmall -> 14f
                R.id.chipReadLarge -> 20f
                else -> 16f
            }
            persistReadingStyle(currentSize, isChecked)
            verseAdapter.updateReadingStyle(currentSize, isChecked)
        }
    }

    private fun persistReadingStyle(size: Float, focusMode: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putFloat(KEY_BIBLE_READ_TEXT_SIZE, size)
            .putBoolean(KEY_BIBLE_READ_FOCUS_MODE, focusMode)
            .apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
