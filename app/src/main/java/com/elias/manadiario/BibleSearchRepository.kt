package com.elias.manadiario

import android.content.Context

object BibleSearchRepository {

    private val books = listOf(
        "genesis" to "Gênesis",
        "exodo" to "Êxodo",
        "levitico" to "Levítico",
        "numeros" to "Números",
        "deuteronomio" to "Deuteronômio",
        "josue" to "Josué",
        "juizes" to "Juízes",
        "rute" to "Rute",
        "1samuel" to "1 Samuel",
        "2samuel" to "2 Samuel",
        "1reis" to "1 Reis",
        "2reis" to "2 Reis",
        "1cronicas" to "1 Crônicas",
        "2cronicas" to "2 Crônicas",
        "esdras" to "Esdras",
        "neemias" to "Neemias",
        "ester" to "Ester",
        "jo" to "Jó",
        "salmos" to "Salmos",
        "proverbios" to "Provérbios",
        "eclesiastes" to "Eclesiastes",
        "canticos" to "Cânticos",
        "isaias" to "Isaías",
        "jeremias" to "Jeremias",
        "lamentacoes" to "Lamentações",
        "ezequiel" to "Ezequiel",
        "daniel" to "Daniel",
        "oseias" to "Oséias",
        "joel" to "Joel",
        "amos" to "Amós",
        "obadias" to "Obadias",
        "jonas" to "Jonas",
        "miqueias" to "Miqueias",
        "naum" to "Naum",
        "habacuque" to "Habacuque",
        "sofonias" to "Sofonias",
        "ageu" to "Ageu",
        "zacarias" to "Zacarias",
        "malaquias" to "Malaquias",
        "mateus" to "Mateus",
        "marcos" to "Marcos",
        "lucas" to "Lucas",
        "joao" to "João",
        "atos" to "Atos",
        "romanos" to "Romanos",
        "1corintios" to "1 Coríntios",
        "2corintios" to "2 Coríntios",
        "galatas" to "Gálatas",
        "efesios" to "Efésios",
        "filipenses" to "Filipenses",
        "colossenses" to "Colossenses",
        "1tessalonicenses" to "1 Tessalonicenses",
        "2tessalonicenses" to "2 Tessalonicenses",
        "1timoteo" to "1 Timóteo",
        "2timoteo" to "2 Timóteo",
        "tito" to "Tito",
        "filemom" to "Filemom",
        "hebreus" to "Hebreus",
        "tiago" to "Tiago",
        "1pedro" to "1 Pedro",
        "2pedro" to "2 Pedro",
        "1joao" to "1 João",
        "2joao" to "2 João",
        "3joao" to "3 João",
        "judas" to "Judas",
        "apocalipse" to "Apocalipse"
    )

    fun search(context: Context, query: String): List<BibleSearchResult> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.length < 3) return emptyList()

        val results = mutableListOf<BibleSearchResult>()

        for ((bookKey, bookName) in books) {
            var chapter = 1

            while (true) {
                val verses = BibleRepository.getVerses(context, bookKey, chapter)

                val invalidChapter = verses.isEmpty() ||
                        verses.first().texto.contains("Capítulo não encontrado", ignoreCase = true) ||
                        verses.first().texto.contains("Erro ao carregar", ignoreCase = true) ||
                        verses.first().texto.contains("Livro não encontrado", ignoreCase = true)

                if (invalidChapter) break

                for (verse in verses) {
                    if (verse.texto.contains(normalizedQuery, ignoreCase = true)) {
                        results.add(
                            BibleSearchResult(
                                bookName = bookName,
                                bookKey = bookKey,
                                chapter = chapter,
                                verse = verse.numero,
                                text = verse.texto
                            )
                        )

                        if (results.size >= 50) {
                            return results
                        }
                    }
                }

                chapter++
            }
        }

        return results
    }
}