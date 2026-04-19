package com.elias.manadiario

import android.content.Context
import java.text.DateFormat
import java.util.Date

data class BibleNote(
    val id: Long,
    val text: String,
    val createdAt: Long
) {
    fun formattedCreatedAt(context: Context): String {
        val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        return context.getString(R.string.bible_notes_created_at, formatter.format(Date(createdAt)))
    }
}
