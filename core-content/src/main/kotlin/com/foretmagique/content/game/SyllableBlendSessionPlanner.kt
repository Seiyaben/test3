package com.foretmagique.content.game

import com.foretmagique.content.catalog.SyllableCatalog
import com.foretmagique.content.model.Syllable
import kotlin.random.Random

object SyllableBlendSessionPlanner {

    // Vowel-only letters have no syllable where they lead as a consonant, so
    // this returns null for them - the "assembler des sons" mini-game only
    // makes sense once a letter can pair with a vowel, matching the
    // "b-a ba" method's actual starting point.
    fun pickSyllableForLetter(letterId: String, random: Random = Random.Default): Syllable? {
        val candidates = SyllableCatalog.syllables.filter { it.consonantLetterId == letterId }
        if (candidates.isEmpty()) return null
        return candidates[random.nextInt(candidates.size)]
    }
}
