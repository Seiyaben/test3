package com.foretmagique.ui.tracing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.components.StarBadge

@Composable
fun TracingScreen(
    letterId: String,
    progressRepository: ProgressRepository,
    onComplete: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: TracingViewModel = viewModel(factory = TracingViewModel.factory(letterId, progressRepository)),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (!state.available) {
                MascotBubble(message = "Le tracé de cette lettre arrive bientôt !")
                Button(onClick = onBack, modifier = Modifier.padding(top = 24.dp)) {
                    Text("Retour")
                }
                return@Surface
            }

            MascotBubble(
                message = when (state.completedWithStars) {
                    null -> "Suis le chemin en pointillés avec ton doigt !"
                    else -> "Bien joué, continue à t'entraîner autant que tu veux !"
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TracingCanvas(
                checkpoints = state.checkpoints,
                resetKey = state.attempt,
                onTraceFinished = viewModel::onTraceFinished,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.completedWithStars != null) {
                StarBadge(stars = state.completedWithStars ?: 0)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = viewModel::onRetry) {
                    Text("Réessayer")
                }
                Button(
                    onClick = { onComplete(state.completedWithStars ?: 0) },
                    modifier = Modifier.padding(top = 12.dp),
                ) {
                    Text("Continuer")
                }
            }

            Button(onClick = onBack, modifier = Modifier.padding(top = 12.dp)) {
                Text("Retour")
            }
        }
    }
}
