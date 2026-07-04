package com.foretmagique.content.model

// ttsText is what a generic TextToSpeech engine is asked to say. It's an
// approximation, not a real isolated phoneme recording: engines read
// consonants as letter names (e.g. French "erre" for "r"), so continuants
// are elongated ("rrr") and occlusives get a neutral schwa ("pe", "te") to
// nudge the engine toward the actual sound. Swap in real recordings later
// via the same id without touching call sites.
data class Phoneme(
    val id: String,
    val label: String,
    val ttsText: String,
)
