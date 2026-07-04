package com.foretmagique.content.model

data class Letter(
    val id: String,
    val upper: Char,
    val lower: Char,
    val soundId: String,
    val order: Int,
)
