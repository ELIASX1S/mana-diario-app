package com.elias.manadiario

sealed class MaisMenuItem {
    data class Secao(val titulo: String) : MaisMenuItem()
    data class Opcao(val opcao: MaisOpcao) : MaisMenuItem()
}
