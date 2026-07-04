package com.foretmagique.di

import android.content.Context
import com.foretmagique.audio.AndroidTtsSpeechService
import com.foretmagique.audio.SpeechService
import com.foretmagique.data.progress.ProgressRepository

// Manual DI container: a single profile, single-Activity app has no need
// for a DI framework - one place to construct the app's few shared
// dependencies is enough.
class AppContainer(context: Context) {
    val speechService: SpeechService = AndroidTtsSpeechService(context)
    val progressRepository: ProgressRepository = ProgressRepository(context)
}
