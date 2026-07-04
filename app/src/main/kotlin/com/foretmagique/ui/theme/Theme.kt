package com.foretmagique.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ForetLightColors = lightColorScheme(
    primary = ForestDeepGreen,
    secondary = ForestSunshine,
    tertiary = ForestMagicPurple,
    background = ForestSky,
    surface = ForestCream,
    error = ForestGentle,
)

private val ForetDarkColors = darkColorScheme(
    primary = ForestLeafGreen,
    secondary = ForestSunshine,
    tertiary = ForestMagicPurple,
    background = ForestBark,
    surface = ForestDeepGreen,
    error = ForestGentle,
)

@Composable
fun ForetMagiqueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) ForetDarkColors else ForetLightColors
    MaterialTheme(
        colorScheme = colors,
        typography = ForetTypography,
        content = content,
    )
}
