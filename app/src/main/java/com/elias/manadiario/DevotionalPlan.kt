package com.elias.manadiario

data class DevotionalPlan(
    val id: String,
    val nome: String,
    val descricao: String,
    val dias: List<DevotionalDay>
)