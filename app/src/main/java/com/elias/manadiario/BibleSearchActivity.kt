package com.elias.manadiario

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BibleSearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var adapter: BibleSearchAdapter

    private val results = mutableListOf<BibleSearchResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_search)

        searchInput = findViewById(R.id.searchInput)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)

        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BibleSearchAdapter(results)
        resultsRecyclerView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                val query = text.toString()

                if (query.length < 3) return

                Thread {

                    val searchResults = BibleSearchRepository.search(applicationContext, query)

                    runOnUiThread {
                        results.clear()
                        results.addAll(searchResults)
                        adapter.notifyDataSetChanged()
                    }

                }.start()
            }
        })
    }
}