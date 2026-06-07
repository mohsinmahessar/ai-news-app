package com.smartreader.ai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Material3 theme. Premium, minimal palette inspired by Kindle/Notion:
 * warm paper light mode, soft charcoal dark mode, a single confident indigo accent.
 * Dark/Light reading modes are first-class because long-form reading demands it.
 */

private val Indigo = Color(0xFF4F46E5)
private val IndigoDark = Color(0xFF818CF8)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    secondary = Color(0xFF0EA5E9),
    background = Color(0xFFFBFAF7),   // warm paper
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFEFEDE7),
)

private val DarkColors = darkColorScheme(
    primary = IndigoDark,
    onPrimary = Color(0xFF11121A),
    secondary = Color(0xFF38BDF8),
    background = Color(0xFF121212),
    surface = Color(0xFF1C1C1E),
    onBackground = Color(0xFFECECEC),
    onSurface = Color(0xFFECECEC),
    surfaceVariant = Color(0xFF2A2A2E),
)

private val AppTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
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
