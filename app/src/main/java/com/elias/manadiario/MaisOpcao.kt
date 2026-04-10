package com.elias.manadiario

import androidx.annotation.DrawableRes

data class MaisOpcao(
    val id: String,
    val titulo: String,
    @DrawableRes val iconRes: Int
)
