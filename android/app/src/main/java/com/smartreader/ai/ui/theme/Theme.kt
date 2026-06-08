package com.smartreader.ai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Premium blue & white Material3 theme.
 *
 * The app defaults to the LIGHT scheme (see MainActivity, which resolves the
 * persisted ThemeMode and passes the boolean here) so the brand always feels
 * bright and premium, with an optional dark mode for night reading.
 */

private val LightColors = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue700,
    secondary = Sky500,
    onSecondary = White,
    background = Slate50,
    onBackground = Slate900,
    surface = White,
    onSurface = Slate900,
    surfaceVariant = Slate200,
    onSurfaceVariant = Slate500,
    outline = Slate200,
    outlineVariant = Slate200,
)

private val DarkColors = darkColorScheme(
    primary = Blue400,
    onPrimary = DeepNavy,
    primaryContainer = Blue700,
    onPrimaryContainer = Blue100,
    secondary = Sky500,
    onSecondary = DeepNavy,
    background = DeepNavy,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200,
    outline = Slate700,
    outlineVariant = Slate700,
)

@Composable
fun SmartReaderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
