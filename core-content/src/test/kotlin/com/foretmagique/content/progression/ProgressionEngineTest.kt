package com.foretmagique.content.progression

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressionEngineTest {

    private val letterOrder = listOf("a", "b", "c")

    @Test
    fun `computeStars gives 3 for a correct first try with no hints`() {
        assertEquals(3, ProgressionEngine.computeStars(correctFirstTry = true, hintsUsed = 0))
    }

    @Test
    fun `computeStars gives 2 when one hint was used`() {
        assertEquals(2, ProgressionEngine.computeStars(correctFirstTry = false, hintsUsed = 1))
    }

    @Test
    fun `computeStars gives 1 when several hints were used`() {
        assertEquals(1, ProgressionEngine.computeStars(correctFirstTry = false, hintsUsed = 3))
    }

    @Test
    fun `initial snapshot only unlocks the first letter`() {
        val snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        assertEquals(setOf("a"), snapshot.unlockedLetterIds)
    }

    @Test
    fun `mastering only one of the two required activities does not unlock the next letter`() {
        var snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SOUND_MATCH, 3, letterOrder)

        assertFalse(ProgressionEngine.isLetterMastered(snapshot, "a"))
        assertEquals(setOf("a"), snapshot.unlockedLetterIds)
    }

    @Test
    fun `mastering both required activities unlocks the next letter`() {
        var snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SOUND_MATCH, 3, letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SYLLABLE_BLEND, 1, letterOrder)

        assertTrue(ProgressionEngine.isLetterMastered(snapshot, "a"))
        assertEquals(setOf("a", "b"), snapshot.unlockedLetterIds)
    }

    @Test
    fun `tracing and word build stars alone do not master a letter`() {
        var snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.TRACING, 3, letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.WORD_BUILD, 3, letterOrder)

        assertFalse(ProgressionEngine.isLetterMastered(snapshot, "a"))
        assertEquals(setOf("a"), snapshot.unlockedLetterIds)
    }

    @Test
    fun `unlock chain stops at the first non-mastered letter`() {
        var snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SOUND_MATCH, 3, letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SYLLABLE_BLEND, 3, letterOrder)
        // "b" is unlocked but not yet mastered, so "c" must stay locked.
        snapshot = ProgressionEngine.withStars(snapshot, "b", ActivityType.SOUND_MATCH, 3, letterOrder)

        assertEquals(setOf("a", "b"), snapshot.unlockedLetterIds)
    }

    @Test
    fun `withStars keeps the best score instead of overwriting with a lower one`() {
        var snapshot = ProgressionEngine.initialSnapshot(letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SOUND_MATCH, 3, letterOrder)
        snapshot = ProgressionEngine.withStars(snapshot, "a", ActivityType.SOUND_MATCH, 1, letterOrder)

        assertEquals(3, snapshot.starsByActivityKey[ProgressionEngine.activityKey("a", ActivityType.SOUND_MATCH)])
    }
}
