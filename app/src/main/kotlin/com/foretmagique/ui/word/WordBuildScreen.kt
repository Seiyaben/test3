package com.foretmagique.ui.word

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foretmagique.audio.SpeechService
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.components.StarBadge
import com.foretmagique.ui.theme.ForestGentle
import com.foretmagique.ui.theme.ForestLeafGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordBuildScreen(
    letterId: String,
    speechService: SpeechService,
    progressRepository: ProgressRepository,
    onComplete: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: WordBuildViewModel = viewModel(factory = WordBuildViewModel.factory(letterId, progressRepository)),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state?.completedWithStars) {
        val current = state
        if (current != null && current.completedWithStars != null) {
            speechService.speak(current.wordId, current.wordText)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        val current = state
        if (current == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                MascotBubble(message = "Il n'y a pas encore de mot pour cette lettre, reviens plus tard !")
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
                    current.completedWithStars != null -> "Bravo, tu as assemblé \"${current.wordText}\" ! 🎉"
                    else -> "Touche les syllabes dans l'ordre pour former le mot ${current.emoji}"
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = current.emoji, fontSize = 64.sp)

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                current.tiles.forEach { tile ->
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = if (tile.used) ForestLeafGreen else ForestGentle,
                        onClick = { if (!tile.used) viewModel.onTileTapped(tile.instanceId) },
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = tile.syllableText,
                                fontSize = 24.sp,
                                color = if (tile.used) Color.White else Color.Black,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (current.completedWithStars != null) {
                StarBadge(stars = current.completedWithStars)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onComplete(current.completedWithStars) }) {
                    Text("Continuer")
                }
            }

            Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
                Text("Retour")
            }
        }
    }
}
