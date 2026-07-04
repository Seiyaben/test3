package com.foretmagique.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AndroidTtsSpeechService(context: Context) : SpeechService {

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isLanguageAvailable = MutableStateFlow(false)
    override val isLanguageAvailable: StateFlow<Boolean> = _isLanguageAvailable.asStateFlow()

    private val tts: TextToSpeech = TextToSpeech(context.applicationContext) { status ->
        onInit(status)
    }

    private fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            _isReady.value = false
            return
        }
        val result = tts.setLanguage(Locale.FRANCE)
        _isLanguageAvailable.value =
            result == TextToSpeech.LANG_AVAILABLE ||
                result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
        _isReady.value = true
    }

    override fun speak(key: String, text: String) {
        if (!_isReady.value || !_isLanguageAvailable.value) return
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, key)
    }

    override fun stop() {
        tts.stop()
    }

    // Not called from Application.onTerminate (unreliable on real devices) -
    // call from MainActivity.onDestroy while isFinishing instead.
    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
