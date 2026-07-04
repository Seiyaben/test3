package com.foretmagique.content.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class SyllableBlendSessionPlannerTest {

    @Test
    fun `picks a syllable led by the requested consonant`() {
        val syllable = SyllableBlendSessionPlanner.pickSyllableForLetter("p", random = Random(5))
        assertTrue(syllable != null && syllable.consonantLetterId == "p")
    }

    @Test
    fun `returns null for a vowel-only letter`() {
        assertNull(SyllableBlendSessionPlanner.pickSyllableForLetter("a", random = Random(5)))
    }

    @Test
    fun `returns null for an unknown letter id`() {
        assertNull(SyllableBlendSessionPlanner.pickSyllableForLetter("z", random = Random(5)))
    }

    @Test
    fun `same seed picks the same syllable`() {
        val first = SyllableBlendSessionPlanner.pickSyllableForLetter("m", random = Random(11))
        val second = SyllableBlendSessionPlanner.pickSyllableForLetter("m", random = Random(11))
        assertEquals(first, second)
    }
}
