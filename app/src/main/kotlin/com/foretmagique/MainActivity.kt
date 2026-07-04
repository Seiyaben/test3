package com.foretmagique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.foretmagique.audio.AndroidTtsSpeechService
import com.foretmagique.navigation.ForetNavHost
import com.foretmagique.ui.theme.ForetMagiqueTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as ForetMagiqueApp).container
        setContent {
            ForetMagiqueTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ForetNavHost(
                        speechService = container.speechService,
                        progressRepository = container.progressRepository,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            val speechService = (application as ForetMagiqueApp).container.speechService
            (speechService as? AndroidTtsSpeechService)?.shutdown()
        }
    }
}
