package com.foretmagique.content.tracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PathScorerTest {

    private val checkpoints = listOf(
        NormalizedPoint(0f, 0f),
        NormalizedPoint(0.5f, 0.5f),
        NormalizedPoint(1f, 1f),
    )

    @Test
    fun `a path that visits every checkpoint in order hits them all`() {
        val drawn = listOf(NormalizedPoint(0f, 0f), NormalizedPoint(0.5f, 0.5f), NormalizedPoint(1f, 1f))
        val result = PathScorer.score(checkpoints, drawn)
        assertEquals(3, result.checkpointsHit)
        assertEquals(1f, result.completionRatio, 0.001f)
    }

    @Test
    fun `a path within tolerance still counts as hitting a checkpoint`() {
        val drawn = listOf(NormalizedPoint(0.02f, 0.02f), NormalizedPoint(0.52f, 0.48f), NormalizedPoint(0.95f, 1f))
        val result = PathScorer.score(checkpoints, drawn)
        assertEquals(3, result.checkpointsHit)
    }

    @Test
    fun `a path that only reaches the first checkpoint scores partially`() {
        val drawn = listOf(NormalizedPoint(0f, 0f), NormalizedPoint(0.05f, 0.05f))
        val result = PathScorer.score(checkpoints, drawn)
        assertEquals(1, result.checkpointsHit)
    }

    @Test
    fun `an empty drawn path hits nothing but never crashes`() {
        val result = PathScorer.score(checkpoints, emptyList())
        assertEquals(0, result.checkpointsHit)
        assertEquals(3, result.totalCheckpoints)
    }

    @Test
    fun `an empty checkpoint list is handled gracefully`() {
        val result = PathScorer.score(emptyList(), listOf(NormalizedPoint(0f, 0f)))
        assertEquals(0, result.totalCheckpoints)
        assertEquals(0f, result.completionRatio, 0.001f)
    }

    @Test
    fun `stars are never zero, even for a poor attempt`() {
        val poorResult = TracingResult(checkpointsHit = 0, totalCheckpoints = 3)
        assertTrue(PathScorer.starsFor(poorResult) >= 1)
    }

    @Test
    fun `a full completion earns 3 stars`() {
        val fullResult = TracingResult(checkpointsHit = 3, totalCheckpoints = 3)
        assertEquals(3, PathScorer.starsFor(fullResult))
    }

    @Test
    fun `a partial completion earns 2 stars`() {
        val partialResult = TracingResult(checkpointsHit = 2, totalCheckpoints = 3)
        assertEquals(2, PathScorer.starsFor(partialResult))
    }
}
