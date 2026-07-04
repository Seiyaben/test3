package com.foretmagique.navigation

object Destinations {
    const val ARG_LETTER_ID = "letterId"

    const val FOREST_MAP = "forest_map"
    const val LETTER_LEARNING = "letter_learning/{$ARG_LETTER_ID}"
    const val SOUND_MATCH = "sound_match/{$ARG_LETTER_ID}"
    const val SYLLABLE_BLEND = "syllable_blend/{$ARG_LETTER_ID}"
    const val WORD_BUILD = "word_build/{$ARG_LETTER_ID}"
    const val TRACING = "tracing/{$ARG_LETTER_ID}"
    const val PROGRESS_OVERVIEW = "progress_overview"

    fun letterLearning(letterId: String) = "letter_learning/$letterId"
    fun soundMatch(letterId: String) = "sound_match/$letterId"
    fun syllableBlend(letterId: String) = "syllable_blend/$letterId"
    fun wordBuild(letterId: String) = "word_build/$letterId"
    fun tracing(letterId: String) = "tracing/$letterId"
}
