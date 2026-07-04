package com.foretmagique.ui.progress

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.components.StarBadge
import com.foretmagique.ui.theme.ForestGentle
import com.foretmagique.ui.theme.ForestLeafGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    viewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory(progressRepository)),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Mes progrès", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            MascotBubble(
                message = "Tu as gagné ${state.totalStars} étoiles et maîtrisé ${state.masteredLetterCount} sur " +
                    "${state.totalLetterCount} lettres. Continue comme ça !",
            )

            Spacer(modifier = Modifier.height(24.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                state.letters.forEach { letter ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = if (letter.mastered) ForestLeafGreen else ForestGentle,
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(text = letter.displayLetter, fontSize = 26.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        StarBadge(stars = letter.bestStars)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onBack) {
                Text("Retour à la forêt")
            }
        }
    }
}
