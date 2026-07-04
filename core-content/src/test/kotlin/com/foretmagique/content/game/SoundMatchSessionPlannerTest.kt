package com.foretmagique.content.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class SoundMatchSessionPlannerTest {

    private val letterIds = listOf("a", "b", "c", "d", "e", "f")

    @Test
    fun `prompt always includes the target letter`() {
        val prompt = SoundMatchSessionPlanner.buildPrompt("c", letterIds, random = Random(42))
        assertTrue(prompt.targetLetterId in prompt.optionLetterIds)
    }

    @Test
    fun `prompt has no duplicate options`() {
        val prompt = SoundMatchSessionPlanner.buildPrompt("c", letterIds, random = Random(1))
        assertEquals(prompt.optionLetterIds.size, prompt.optionLetterIds.toSet().size)
    }

    @Test
    fun `prompt respects requested option count when enough letters are available`() {
        val prompt = SoundMatchSessionPlanner.buildPrompt("a", letterIds, optionCount = 4, random = Random(7))
        assertEquals(4, prompt.optionLetterIds.size)
    }

    @Test
    fun `prompt shrinks gracefully when fewer letters are available than requested`() {
        val smallPool = listOf("x", "y")
        val prompt = SoundMatchSessionPlanner.buildPrompt("x", smallPool, optionCount = 4, random = Random(3))
        assertEquals(2, prompt.optionLetterIds.size)
        assertTrue("x" in prompt.optionLetterIds)
        assertTrue("y" in prompt.optionLetterIds)
    }

    @Test
    fun `same seed produces the same prompt`() {
        val first = SoundMatchSessionPlanner.buildPrompt("c", letterIds, random = Random(99))
        val second = SoundMatchSessionPlanner.buildPrompt("c", letterIds, random = Random(99))
        assertEquals(first, second)
    }
}
