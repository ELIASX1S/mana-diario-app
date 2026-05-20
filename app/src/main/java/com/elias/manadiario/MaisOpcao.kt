package com.elias.manadiario

import androidx.annotation.DrawableRes

data class MaisOpcao(
    val id: String,
    val titulo: String,
    @DrawableRes val iconRes: Int
) {
    companion object {
        const val ID_THEMES = "temas"
        const val ID_SEARCH_BIBLE = "buscar_biblia"
        const val ID_FAVORITE_VERSES = "versiculos_favoritos"
        const val ID_PROMISE_BOX = "caixinha"
        const val ID_FAVORITES = "favoritos"
        const val ID_FAITH_MESSAGE = "mensagem_fe"
        const val ID_SETTINGS = "configuracoes"
    }
}
