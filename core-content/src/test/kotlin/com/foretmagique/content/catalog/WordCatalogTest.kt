package com.foretmagique.content.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WordCatalogTest {

    @Test
    fun `word ids are unique`() {
        val ids = WordCatalog.words.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every word syllable id resolves to a known syllable`() {
        val syllableIds = SyllableCatalog.syllables.map { it.id }.toSet()
        WordCatalog.words.forEach { word ->
            word.syllableIds.forEach { syllableId ->
                assertTrue("word ${word.id} references unknown syllable $syllableId", syllableId in syllableIds)
            }
        }
    }

    @Test
    fun `every syllable's letter ids resolve to known letters`() {
        val letterIds = AlphabetCatalog.letters.map { it.id }.toSet()
        SyllableCatalog.syllables.forEach { syllable ->
            assertTrue("syllable ${syllable.id} vowel ${syllable.vowelLetterId} unknown", syllable.vowelLetterId in letterIds)
            syllable.consonantLetterId?.let { consonantId ->
                assertTrue("syllable ${syllable.id} consonant $consonantId unknown", consonantId in letterIds)
            }
        }
    }

    @Test
    fun `every word has at least one syllable`() {
        WordCatalog.words.forEach { word ->
            assertTrue("word ${word.id} has no syllables", word.syllableIds.isNotEmpty())
        }
    }

    @Test
    fun `wordsForLetter finds papa for letter p`() {
        val words = WordCatalog.wordsForLetter("p")
        assertTrue(words.any { it.id == "papa" })
    }
}
