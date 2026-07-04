package com.foretmagique.ui.forestmap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.components.MascotBubble
import com.foretmagique.ui.components.StarBadge
import com.foretmagique.ui.theme.ForestLeafGreen
import com.foretmagique.ui.theme.ForestMossGreen
import com.foretmagique.ui.theme.ForestSky

@Composable
fun ForestMapScreen(
    progressRepository: ProgressRepository,
    onLetterSelected: (String) -> Unit,
    onOpenProgress: () -> Unit,
    viewModel: ForestMapViewModel = viewModel(factory = ForestMapViewModel.factory(progressRepository)),
) {
    val clearings by viewModel.clearings.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ForestSky, ForestMossGreen))),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Forêt Magique", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = onOpenProgress) {
                    Text(text = "⭐", fontSize = 28.sp)
                }
            }

            MascotBubble(
                message = "Bienvenue dans la forêt magique ! Choisis une clairière pour commencer.",
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(clearings, key = { it.letterId }) { clearing ->
                    ClearingTile(
                        clearing = clearing,
                        onClick = { if (clearing.unlocked) onLetterSelected(clearing.letterId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ClearingTile(clearing: ClearingUiState, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .clickable(enabled = clearing.unlocked, onClick = onClick),
            color = if (clearing.unlocked) ForestLeafGreen else Color.Gray.copy(alpha = 0.4f),
            shape = CircleShape,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (clearing.unlocked) clearing.displayLetter else "🔒",
                    fontSize = if (clearing.unlocked) 32.sp else 28.sp,
                    color = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        StarBadge(stars = clearing.stars)
    }
}
