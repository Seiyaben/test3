package com.foretmagique.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun StarBadge(stars: Int, modifier: Modifier = Modifier, maxStars: Int = 3) {
    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            Text(text = if (index < stars) "⭐" else "☆", fontSize = 16.sp)
        }
    }
}
