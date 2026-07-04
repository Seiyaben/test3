package com.foretmagique.audio

import kotlinx.coroutines.flow.StateFlow

// key identifies the sound logically (e.g. a phoneme or word id) so a future
// implementation can look up a real recorded file under res/raw/<key> and
// only fall back to speaking `text` via TTS when no recording exists -
// swappable without touching any call site.
interface SpeechService {
    val isReady: StateFlow<Boolean>
    val isLanguageAvailable: StateFlow<Boolean>

    fun speak(key: String, text: String)
    fun stop()
}
