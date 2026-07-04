package com.foretmagique.ui.letter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foretmagique.audio.SpeechService
import com.foretmagique.content.catalog.AlphabetCatalog
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.theme.ForestSunshine

@Composable
fun LetterLearningScreen(
    letterId: String,
    speechService: SpeechService,
    onPlaySoundMatch: () -> Unit,
    onPlaySyllableBlend: () -> Unit,
    onPlayWordBuild: () -> Unit,
    onPlayTracing: () -> Unit,
    onBack: () -> Unit,
) {
    val letter = AlphabetCatalog.letterById(letterId)
    val phoneme = AlphabetCatalog.phonemeForLetter(letterId)

    fun playSound() = speechService.speak(phoneme.id, phoneme.ttsText)

    LaunchedEffect(letterId) { playSound() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            MascotBubble(message = "Écoute bien le son de cette lettre !")

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.padding(8.dp),
                shape = CircleShape,
                color = ForestSunshine,
                onClick = { playSound() },
            ) {
                Box(
                    modifier = Modifier.padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${letter.upper}${letter.lower}",
                        fontSize = 96.sp,
                        style = MaterialTheme.typography.displayLarge,
                    )
                }
            }

            Text(
                text = "Touche la lettre pour réécouter son son 🔊",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
            )

            Button(onClick = onPlaySoundMatch, modifier = Modifier.padding(top = 32.dp)) {
                Text("🎧 Écouter et associer")
            }

            Button(onClick = onPlaySyllableBlend, modifier = Modifier.padding(top = 12.dp)) {
                Text("🔤 Assembler des sons")
            }

            Button(onClick = onPlayWordBuild, modifier = Modifier.padding(top = 12.dp)) {
                Text("🧩 Assembler un mot")
            }

            Button(onClick = onPlayTracing, modifier = Modifier.padding(top = 12.dp)) {
                Text("✏️ Écrire")
            }

            Button(onClick = onBack, modifier = Modifier.padding(top = 12.dp)) {
                Text("Retour à la forêt")
            }
        }
    }
}
