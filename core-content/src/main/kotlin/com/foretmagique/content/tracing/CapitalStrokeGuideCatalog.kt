package com.foretmagique.content.tracing

// Each guide is a single continuous polyline approximating the letter's
// dominant stroke, meant to be traced in one uninterrupted drag - not a
// faithful multi-stroke font (e.g. "A" skips its crossbar, "T" backtracks
// along the top bar before dropping down). Good enough for a lenient,
// always-rewarding tracing game, not for calligraphy grading.
object CapitalStrokeGuideCatalog {

    private fun p(x: Float, y: Float) = NormalizedPoint(x, y)

    val guides: List<LetterStrokeGuide> = listOf(
        LetterStrokeGuide("a", listOf(p(0f, 1f), p(0.25f, 0.5f), p(0.5f, 0f), p(0.75f, 0.5f), p(1f, 1f))),
        LetterStrokeGuide("e", listOf(p(1f, 0f), p(0f, 0f), p(0f, 1f), p(1f, 1f))),
        LetterStrokeGuide("i", listOf(p(0.5f, 0f), p(0.5f, 1f))),
        LetterStrokeGuide(
            "o",
            listOf(
                p(0.5f, 0f), p(0.85f, 0.15f), p(1f, 0.5f), p(0.85f, 0.85f),
                p(0.5f, 1f), p(0.15f, 0.85f), p(0f, 0.5f), p(0.15f, 0.15f), p(0.5f, 0f),
            ),
        ),
        LetterStrokeGuide(
            "u",
            listOf(p(0f, 0f), p(0f, 0.7f), p(0.15f, 0.95f), p(0.5f, 1f), p(0.85f, 0.95f), p(1f, 0.7f), p(1f, 0f)),
        ),
        LetterStrokeGuide("l", listOf(p(0f, 0f), p(0f, 1f), p(1f, 1f))),
        LetterStrokeGuide("m", listOf(p(0f, 1f), p(0f, 0f), p(0.5f, 0.6f), p(1f, 0f), p(1f, 1f))),
        LetterStrokeGuide(
            "p",
            listOf(p(0f, 1f), p(0f, 0f), p(0.7f, 0.05f), p(0.9f, 0.3f), p(0.7f, 0.55f), p(0f, 0.5f)),
        ),
        LetterStrokeGuide("t", listOf(p(0f, 0f), p(1f, 0f), p(0.5f, 0f), p(0.5f, 1f))),
        LetterStrokeGuide(
            "r",
            listOf(
                p(0f, 1f), p(0f, 0f), p(0.7f, 0.05f), p(0.9f, 0.3f), p(0.7f, 0.55f), p(0f, 0.5f), p(0.6f, 1f),
            ),
        ),
        LetterStrokeGuide("n", listOf(p(0f, 1f), p(0f, 0f), p(1f, 1f), p(1f, 0f))),
        LetterStrokeGuide(
            "s",
            listOf(
                p(0.9f, 0.1f), p(0.5f, 0f), p(0.1f, 0.15f), p(0.1f, 0.4f),
                p(0.5f, 0.5f), p(0.9f, 0.6f), p(0.9f, 0.85f), p(0.5f, 1f), p(0.1f, 0.9f),
            ),
        ),
    )

    fun guideForLetter(letterId: String): LetterStrokeGuide? =
        guides.firstOrNull { it.letterId == letterId }
}
