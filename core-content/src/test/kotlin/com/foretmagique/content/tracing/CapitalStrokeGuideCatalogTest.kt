package com.foretmagique.content.tracing

import com.foretmagique.content.catalog.AlphabetCatalog
import org.junit.Assert.assertTrue
import org.junit.Test

class CapitalStrokeGuideCatalogTest {

    @Test
    fun `every MVP letter has a stroke guide`() {
        AlphabetCatalog.letters.forEach { letter ->
            assertTrue(
                "no stroke guide for letter ${letter.id}",
                CapitalStrokeGuideCatalog.guideForLetter(letter.id) != null,
            )
        }
    }

    @Test
    fun `every guide has at least two checkpoints`() {
        CapitalStrokeGuideCatalog.guides.forEach { guide ->
            assertTrue("guide for ${guide.letterId} has too few checkpoints", guide.checkpoints.size >= 2)
        }
    }

    @Test
    fun `every checkpoint is within the normalized 0 to 1 bounding box`() {
        CapitalStrokeGuideCatalog.guides.forEach { guide ->
            guide.checkpoints.forEach { point ->
                assertTrue("${guide.letterId} has an out-of-range x", point.x in 0f..1f)
                assertTrue("${guide.letterId} has an out-of-range y", point.y in 0f..1f)
            }
        }
    }

    @Test
    fun `unknown letter id returns null`() {
        assertTrue(CapitalStrokeGuideCatalog.guideForLetter("does-not-exist") == null)
    }
}
