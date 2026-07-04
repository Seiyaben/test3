package com.foretmagique.content.game

import com.foretmagique.content.catalog.WordCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class WordBuildSessionPlannerTest {

    @Test
    fun `picks a word that actually contains the requested letter`() {
        val word = WordBuildSessionPlanner.pickWordForLetter("p", random = Random(2))
        assertTrue(word != null && WordCatalog.wordsForLetter("p").any { it.id == word.id })
    }

    @Test
    fun `returns null when no word matches the letter`() {
        assertNull(WordBuildSessionPlanner.pickWordForLetter("does-not-exist", random = Random(2)))
    }

    @Test
    fun `prompt tiles always contain every target syllable`() {
        val word = WordCatalog.wordById("papa")
        val prompt = WordBuildSessionPlanner.buildPrompt(word, random = Random(3))
        word.syllableIds.forEach { syllableId ->
            assertTrue(syllableId in prompt.tileSyllableIds)
        }
    }

    @Test
    fun `prompt adds distractor tiles beyond the target syllables`() {
        val word = WordCatalog.wordById("papa")
        val prompt = WordBuildSessionPlanner.buildPrompt(word, distractorCount = 2, random = Random(3))
        assertEquals(word.syllableIds.size + 2, prompt.tileSyllableIds.size)
    }
}
