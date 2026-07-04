package com.foretmagique.ui.matching

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foretmagique.audio.SpeechService
import com.foretmagique.content.catalog.AlphabetCatalog
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.components.StarBadge
import com.foretmagique.ui.theme.ForestLeafGreen
import com.foretmagique.ui.theme.ForestSunshine

@Composable
fun SoundMatchScreen(
    letterId: String,
    speechService: SpeechService,
    progressRepository: ProgressRepository,
    onComplete: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: SoundMatchViewModel = viewModel(factory = SoundMatchViewModel.factory(letterId, progressRepository)),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    fun playTargetSound() {
        val phoneme = AlphabetCatalog.phonemeForLetter(state.targetLetterId)
        speechService.speak(phoneme.id, phoneme.ttsText)
    }

    LaunchedEffect(state.targetLetterId) { playTargetSound() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            MascotBubble(
                message = when (state.feedback) {
                    SoundMatchFeedback.CORRECT -> "Bravo, tu as trouvé le bon son ! 🎉"
                    SoundMatchFeedback.INCORRECT -> "Presque ! Essaie encore, écoute bien 👂"
                    null -> "Écoute le son et touche la bonne lettre."
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = ForestSunshine,
                onClick = { playTargetSound() },
            ) {
                Text(
                    text = "🔊 Réécouter",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (state.completedWithStars != null) {
                StarBadge(stars = state.completedWithStars ?: 0)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onComplete(state.completedWithStars ?: 0) }) {
                    Text("Continuer")
                }
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    state.optionLetterIds.forEach { optionId ->
                        val letter = AlphabetCatalog.letterById(optionId)
                        Surface(
                            modifier = Modifier.size(88.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = ForestLeafGreen,
                            onClick = { viewModel.onOptionSelected(optionId) },
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(text = "${letter.upper}${letter.lower}", fontSize = 32.sp)
                            }
                        }
                    }
                }
            }

            Button(onClick = onBack, modifier = Modifier.padding(top = 32.dp)) {
                Text("Retour")
            }
        }
    }
}
