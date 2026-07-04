package com.foretmagique.ui.syllable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
fun SyllableBlendScreen(
    letterId: String,
    speechService: SpeechService,
    progressRepository: ProgressRepository,
    onComplete: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: SyllableBlendViewModel = viewModel(factory = SyllableBlendViewModel.factory(letterId, progressRepository)),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.completedWithStars) {
        if (state.completedWithStars != null) {
            speechService.speak(state.syllableId, state.syllableText)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!state.available) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                MascotBubble(message = "Reviens ici une fois que tu connais une consonne, pour assembler des sons !")
                Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
                    Text("Retour")
                }
            }
            return@Surface
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            MascotBubble(
                message = when {
                    state.completedWithStars != null -> "Bravo, tu as fabriqué le son \"${state.syllableText}\" ! 🎉"
                    state.tappedCount == 0 -> "Touche les deux sons dans l'ordre pour les assembler."
                    else -> "Encore un ! Touche le deuxième son."
                },
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                state.tileOrder.forEach { kind ->
                    val letterIdForTile = if (kind == SyllableTileKind.CONSONANT) state.consonantPhonemeId else state.vowelPhonemeId
                    val letter = AlphabetCatalog.letterById(letterIdForTile)
                    val phoneme = AlphabetCatalog.phonemeForLetter(letterIdForTile)
                    val alreadyTapped = when (kind) {
                        SyllableTileKind.CONSONANT -> state.tappedCount >= 1
                        SyllableTileKind.VOWEL -> state.tappedCount >= 2
                    }
                    Surface(
                        modifier = Modifier.size(96.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = if (alreadyTapped) ForestLeafGreen else ForestSunshine,
                        onClick = {
                            speechService.speak(phoneme.id, phoneme.ttsText)
                            viewModel.onTileTapped(kind)
                        },
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = letter.lower.toString(), fontSize = 40.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (state.completedWithStars != null) {
                StarBadge(stars = state.completedWithStars ?: 0)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onComplete(state.completedWithStars ?: 0) }) {
                    Text("Continuer")
                }
            }

            Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
                Text("Retour")
            }
        }
    }
}
