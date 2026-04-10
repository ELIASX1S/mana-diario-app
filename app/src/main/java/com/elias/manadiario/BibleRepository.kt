package com.elias.manadiario

import android.content.Context
import org.json.JSONArray

object BibleRepository {

    @Volatile
    private var bibleData: JSONArray? = null

    private val bookKeysInOrder = listOf(
        "genesis",
        "exodo",
        "levitico",
        "numeros",
        "deuteronomio",
        "josue",
        "juizes",
        "rute",
        "1samuel",
        "2samuel",
        "1reis",
        "2reis",
        "1cronicas",
        "2cronicas",
        "esdras",
        "neemias",
        "ester",
        "jo",
        "salmos",
        "proverbios",
        "eclesiastes",
        "canticos",
        "isaias",
        "jeremias",
        "lamentacoes",
        "ezequiel",
        "daniel",
        "oseias",
        "joel",
        "amos",
        "obadias",
        "jonas",
        "miqueias",
        "naum",
        "habacuque",
        "sofonias",
        "ageu",
        "zacarias",
        "malaquias",
        "mateus",
        "marcos",
        "lucas",
        "joao",
        "atos",
        "romanos",
        "1corintios",
        "2corintios",
        "galatas",
        "efesios",
        "filipenses",
        "colossenses",
        "1tessalonicenses",
        "2tessalonicenses",
        "1timoteo",
        "2timoteo",
        "tito",
        "filemom",
        "hebreus",
        "tiago",
        "1pedro",
        "2pedro",
        "1joao",
        "2joao",
        "3joao",
        "judas",
        "apocalipse"
    )

    fun preload(context: Context) {
        ensureBibleLoaded(context.applicationContext)
    }

    fun isLoaded(): Boolean {
        return bibleData != null
    }

    fun getVerses(
        context: Context,
        bookKey: String,
        chapterNumber: Int
    ): List<BibleVerse> {

        ensureBibleLoaded(context.applicationContext)

        val verses = mutableListOf<BibleVerse>()

        try {
            val bookIndex = bookKeysInOrder.indexOf(bookKey)

            if (bookIndex == -1) {
                return listOf(
                    BibleVerse(1, "Livro não encontrado."),
                    BibleVerse(2, "Chave recebida: $bookKey")
                )
            }

            val book = bibleData!!.getJSONObject(bookIndex)
            val chapters = book.getJSONArray("capitulos")

            val chapterIndex = chapterNumber - 1

            if (chapterIndex < 0 || chapterIndex >= chapters.length()) {
                return listOf(
                    BibleVerse(1, "Capítulo não encontrado para este livro.")
                )
            }

            val chapter = chapters.getJSONArray(chapterIndex)

            for (i in 0 until chapter.length()) {
                val verseText = chapter.getString(i)

                verses.add(
                    BibleVerse(
                        numero = i + 1,
                        texto = verseText
                    )
                )
            }

        } catch (e: Exception) {
            return listOf(
                BibleVerse(1, "Erro ao carregar capítulo."),
                BibleVerse(2, "Detalhe: ${e.message ?: "erro desconhecido"}")
            )
        }

        return if (verses.isEmpty()) {
            listOf(BibleVerse(1, "Nenhum versículo encontrado."))
        } else {
            verses
        }
    }

    @Synchronized
    private fun ensureBibleLoaded(context: Context) {
        if (bibleData != null) return

        val json = context.assets
            .open("biblialivre.json")
            .bufferedReader()
            .use { it.readText() }

        bibleData = JSONArray(json)
    }
}