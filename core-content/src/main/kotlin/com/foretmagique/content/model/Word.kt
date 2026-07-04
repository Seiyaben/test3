package com.foretmagique.content.model

// syllableIds are the spoken building blocks used by the word-building
// mini-game; they don't always concatenate back to the exact spelling
// (e.g. "tapis" -> ["ta", "pi"], silent final "s"), matching how the word
// actually sounds rather than how it's written.
data class Word(
    val id: String,
    val text: String,
    val syllableIds: List<String>,
    val emoji: String,
    val difficultyTier: Int,
)
