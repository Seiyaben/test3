package com.foretmagique.content.game

import com.foretmagique.content.catalog.SyllableCatalog
import com.foretmagique.content.catalog.WordCatalog
import com.foretmagique.content.model.Word
import kotlin.random.Random

data class WordBuildPrompt(
    val wordId: String,
    val targetSyllableIds: List<String>,
    val tileSyllableIds: List<String>,
)

object WordBuildSessionPlanner {

    fun pickWordForLetter(letterId: String, random: Random = Random.Default): Word? {
        val candidates = WordCatalog.wordsForLetter(letterId)
        if (candidates.isEmpty()) return null
        return candidates[random.nextInt(candidates.size)]
    }

    fun buildPrompt(
        word: Word,
        distractorCount: Int = 2,
        random: Random = Random.Default,
    ): WordBuildPrompt {
        val distractorPool = SyllableCatalog.syllables.map { it.id }.filter { it !in word.syllableIds }
        val distractors = distractorPool.shuffled(random).take(distractorCount.coerceAtMost(distractorPool.size))
        val tiles = (word.syllableIds + distractors).shuffled(random)
        return WordBuildPrompt(wordId = word.id, targetSyllableIds = word.syllableIds, tileSyllableIds = tiles)
    }
}
