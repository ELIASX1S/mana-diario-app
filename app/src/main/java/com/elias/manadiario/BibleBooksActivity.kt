package com.elias.manadiario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BibleBooksActivity : AppCompatActivity() {

    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var adapter: BibleBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bible_books)

        val toolbar: Toolbar = findViewById(R.id.bibleBooksToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bíblia"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        booksRecyclerView = findViewById(R.id.bibleBooksRecyclerView)
        booksRecyclerView.layoutManager = LinearLayoutManager(this)

        val books = getBibleBooks()
        adapter = BibleBookAdapter(books)
        booksRecyclerView.adapter = adapter

        // Pré-carrega a Bíblia em segundo plano
        Thread {
            BibleRepository.preload(applicationContext)
        }.start()
    }

    private fun getBibleBooks(): List<BibleBook> {
        return listOf(
            BibleBook("Gênesis", "genesis", 50),
            BibleBook("Êxodo", "exodo", 40),
            BibleBook("Levítico", "levitico", 27),
            BibleBook("Números", "numeros", 36),
            BibleBook("Deuteronômio", "deuteronomio", 34),
            BibleBook("Josué", "josue", 24),
            BibleBook("Juízes", "juizes", 21),
            BibleBook("Rute", "rute", 4),
            BibleBook("1 Samuel", "1samuel", 31),
            BibleBook("2 Samuel", "2samuel", 24),
            BibleBook("1 Reis", "1reis", 22),
            BibleBook("2 Reis", "2reis", 25),
            BibleBook("1 Crônicas", "1cronicas", 29),
            BibleBook("2 Crônicas", "2cronicas", 36),
            BibleBook("Esdras", "esdras", 10),
            BibleBook("Neemias", "neemias", 13),
            BibleBook("Ester", "ester", 10),
            BibleBook("Jó", "jo", 42),
            BibleBook("Salmos", "salmos", 150),
            BibleBook("Provérbios", "proverbios", 31),
            BibleBook("Eclesiastes", "eclesiastes", 12),
            BibleBook("Cânticos", "canticos", 8),
            BibleBook("Isaías", "isaias", 66),
            BibleBook("Jeremias", "jeremias", 52),
            BibleBook("Lamentações", "lamentacoes", 5),
            BibleBook("Ezequiel", "ezequiel", 48),
            BibleBook("Daniel", "daniel", 12),
            BibleBook("Oséias", "oseias", 14),
            BibleBook("Joel", "joel", 3),
            BibleBook("Amós", "amos", 9),
            BibleBook("Obadias", "obadias", 1),
            BibleBook("Jonas", "jonas", 4),
            BibleBook("Miqueias", "miqueias", 7),
            BibleBook("Naum", "naum", 3),
            BibleBook("Habacuque", "habacuque", 3),
            BibleBook("Sofonias", "sofonias", 3),
            BibleBook("Ageu", "ageu", 2),
            BibleBook("Zacarias", "zacarias", 14),
            BibleBook("Malaquias", "malaquias", 4),
            BibleBook("Mateus", "mateus", 28),
            BibleBook("Marcos", "marcos", 16),
            BibleBook("Lucas", "lucas", 24),
            BibleBook("João", "joao", 21),
            BibleBook("Atos", "atos", 28),
            BibleBook("Romanos", "romanos", 16),
            BibleBook("1 Coríntios", "1corintios", 16),
            BibleBook("2 Coríntios", "2corintios", 13),
            BibleBook("Gálatas", "galatas", 6),
            BibleBook("Efésios", "efesios", 6),
            BibleBook("Filipenses", "filipenses", 4),
            BibleBook("Colossenses", "colossenses", 4),
            BibleBook("1 Tessalonicenses", "1tessalonicenses", 5),
            BibleBook("2 Tessalonicenses", "2tessalonicenses", 3),
            BibleBook("1 Timóteo", "1timoteo", 6),
            BibleBook("2 Timóteo", "2timoteo", 4),
            BibleBook("Tito", "tito", 3),
            BibleBook("Filemom", "filemom", 1),
            BibleBook("Hebreus", "hebreus", 13),
            BibleBook("Tiago", "tiago", 5),
            BibleBook("1 Pedro", "1pedro", 5),
            BibleBook("2 Pedro", "2pedro", 3),
            BibleBook("1 João", "1joao", 5),
            BibleBook("2 João", "2joao", 1),
            BibleBook("3 João", "3joao", 1),
            BibleBook("Judas", "judas", 1),
            BibleBook("Apocalipse", "apocalipse", 22)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}