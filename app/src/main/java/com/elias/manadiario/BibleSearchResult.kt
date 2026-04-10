package com.elias.manadiario

data class BibleSearchResult(
    val bookName: String,
    val bookKey: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)