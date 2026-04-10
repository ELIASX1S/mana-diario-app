package com.elias.manadiario

import android.content.Context
import androidx.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONObject

object BibleFavoritesManager {

    private const val KEY_BIBLE_FAVORITES = "bible_favorite_verses"

    fun addFavorite(
        context: Context,
        bookName: String,
        chapterNumber: Int,
        verseNumber: Int,
        verseText: String
    ) {
        val favorites = getFavorites(context).toMutableList()

        val alreadyExists = favorites.any {
            it.bookName == bookName &&
                    it.chapterNumber == chapterNumber &&
                    it.verseNumber == verseNumber
        }

        if (alreadyExists) return

        favorites.add(
            FavoriteBibleVerse(
                bookName = bookName,
                chapterNumber = chapterNumber,
                verseNumber = verseNumber,
                verseText = verseText
            )
        )

        saveFavorites(context, favorites)
    }

    fun removeFavorite(
        context: Context,
        bookName: String,
        chapterNumber: Int,
        verseNumber: Int
    ) {
        val favorites = getFavorites(context).toMutableList()

        favorites.removeAll {
            it.bookName == bookName &&
                    it.chapterNumber == chapterNumber &&
                    it.verseNumber == verseNumber
        }

        saveFavorites(context, favorites)
    }

    fun isFavorite(
        context: Context,
        bookName: String,
        chapterNumber: Int,
        verseNumber: Int
    ): Boolean {
        return getFavorites(context).any {
            it.bookName == bookName &&
                    it.chapterNumber == chapterNumber &&
                    it.verseNumber == verseNumber
        }
    }

    fun getFavorites(context: Context): List<FavoriteBibleVerse> {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonString = preferences.getString(KEY_BIBLE_FAVORITES, null) ?: return emptyList()

        return try {
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<FavoriteBibleVerse>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                list.add(
                    FavoriteBibleVerse(
                        bookName = item.getString("bookName"),
                        chapterNumber = item.getInt("chapterNumber"),
                        verseNumber = item.getInt("verseNumber"),
                        verseText = item.getString("verseText")
                    )
                )
            }

            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveFavorites(context: Context, favorites: List<FavoriteBibleVerse>) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonArray = JSONArray()

        favorites.forEach { favorite ->
            val obj = JSONObject().apply {
                put("bookName", favorite.bookName)
                put("chapterNumber", favorite.chapterNumber)
                put("verseNumber", favorite.verseNumber)
                put("verseText", favorite.verseText)
            }
            jsonArray.put(obj)
        }

        preferences.edit()
            .putString(KEY_BIBLE_FAVORITES, jsonArray.toString())
            .apply()
    }
}