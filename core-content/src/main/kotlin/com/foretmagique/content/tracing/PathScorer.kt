package com.foretmagique.content.tracing

import kotlin.math.sqrt

data class TracingResult(val checkpointsHit: Int, val totalCheckpoints: Int) {
    val completionRatio: Float
        get() = if (totalCheckpoints == 0) 0f else checkpointsHit.toFloat() / totalCheckpoints
}

// Deliberately not a handwriting-recognition model: a child's drawn path
// just needs to sweep near each checkpoint in order, within a generous
// tolerance. Tracing never blocks progression, so this only decides how
// many (of always at least one) stars to award, never pass/fail.
object PathScorer {

    fun score(
        checkpoints: List<NormalizedPoint>,
        drawnPath: List<NormalizedPoint>,
        tolerance: Float = 0.18f,
    ): TracingResult {
        if (checkpoints.isEmpty()) return TracingResult(checkpointsHit = 0, totalCheckpoints = 0)

        var nextCheckpointIndex = 0
        for (point in drawnPath) {
            if (nextCheckpointIndex >= checkpoints.size) break
            if (distance(point, checkpoints[nextCheckpointIndex]) <= tolerance) {
                nextCheckpointIndex++
            }
        }
        return TracingResult(checkpointsHit = nextCheckpointIndex, totalCheckpoints = checkpoints.size)
    }

    fun starsFor(result: TracingResult): Int = when {
        result.completionRatio >= 0.9f -> 3
        result.completionRatio >= 0.5f -> 2
        else -> 1
    }

    private fun distance(a: NormalizedPoint, b: NormalizedPoint): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }
}
