package com.foretmagique.content.game

import kotlin.random.Random

data class SoundMatchPrompt(
    val targetLetterId: String,
    val optionLetterIds: List<String>,
)

object SoundMatchSessionPlanner {

    fun buildPrompt(
        targetLetterId: String,
        allLetterIds: List<String>,
        optionCount: Int = 4,
        random: Random = Random.Default,
    ): SoundMatchPrompt {
        require(targetLetterId in allLetterIds) { "targetLetterId must be part of allLetterIds" }

        val distractorPool = allLetterIds.filter { it != targetLetterId }
        val distractorCount = (optionCount - 1).coerceAtMost(distractorPool.size)
        val distractors = distractorPool.shuffled(random).take(distractorCount)
        val options = (distractors + targetLetterId).shuffled(random)

        return SoundMatchPrompt(targetLetterId = targetLetterId, optionLetterIds = options)
    }
}
