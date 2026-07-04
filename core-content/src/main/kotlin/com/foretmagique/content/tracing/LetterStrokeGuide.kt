package com.foretmagique.content.tracing

data class LetterStrokeGuide(
    val letterId: String,
    val checkpoints: List<NormalizedPoint>,
)
