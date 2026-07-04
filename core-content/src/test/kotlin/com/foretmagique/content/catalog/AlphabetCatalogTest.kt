package com.foretmagique.content.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlphabetCatalogTest {

    @Test
    fun `letter ids are unique`() {
        val ids = AlphabetCatalog.letters.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `teaching order is a valid permutation of 1 through n`() {
        val orders = AlphabetCatalog.letters.map { it.order }.sorted()
        assertEquals((1..AlphabetCatalog.letters.size).toList(), orders)
    }

    @Test
    fun `every letter's soundId resolves to a known phoneme`() {
        val phonemeIds = AlphabetCatalog.phonemes.map { it.id }.toSet()
        AlphabetCatalog.letters.forEach { letter ->
            assertTrue("letter ${letter.id} references unknown phoneme ${letter.soundId}", letter.soundId in phonemeIds)
        }
    }

    @Test
    fun `phonemeForLetter returns the matching phoneme`() {
        val phoneme = AlphabetCatalog.phonemeForLetter("m")
        assertEquals("m", phoneme.id)
    }

    @Test
    fun `lettersInTeachingOrder is sorted ascending by order`() {
        val orders = AlphabetCatalog.lettersInTeachingOrder.map { it.order }
        assertEquals(orders.sorted(), orders)
    }
}
