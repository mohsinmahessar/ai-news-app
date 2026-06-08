@file:OptIn(ExperimentalTextApi::class)

package com.smartreader.ai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.smartreader.ai.R

/**
 * Premium typography using Plus Jakarta Sans (bundled as a single variable font).
 * Compose maps each [FontWeight] onto the variable font's `wght` axis, so we get
 * real Medium/SemiBold/Bold weights from one ~170KB file — fully offline.
 */
private fun jakarta(weight: FontWeight) = Font(
    R.font.plus_jakarta_sans,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val Jakarta = FontFamily(
    jakarta(FontWeight.Normal),
    jakarta(FontWeight.Medium),
    jakarta(FontWeight.SemiBold),
    jakarta(FontWeight.Bold),
    jakarta(FontWeight.ExtraBold),
)

/** Material3 typography rebuilt on the Jakarta family with a premium, tighter scale. */
val AppTypography: Typography = Typography().run {
    copy(
        displaySmall = displaySmall.copy(fontFamily = Jakarta, fontWeight = FontWeight.ExtraBold),
        headlineLarge = headlineLarge.copy(fontFamily = Jakarta, fontWeight = FontWeight.Bold),
        headlineMedium = headlineMedium.copy(fontFamily = Jakarta, fontWeight = FontWeight.Bold),
        headlineSmall = headlineSmall.copy(fontFamily = Jakarta, fontWeight = FontWeight.Bold),
        titleLarge = titleLarge.copy(fontFamily = Jakarta, fontWeight = FontWeight.SemiBold),
        titleMedium = titleMedium.copy(fontFamily = Jakarta, fontWeight = FontWeight.SemiBold),
        titleSmall = titleSmall.copy(fontFamily = Jakarta, fontWeight = FontWeight.Medium),
        bodyLarge = bodyLarge.copy(fontFamily = Jakarta),
        bodyMedium = bodyMedium.copy(fontFamily = Jakarta),
        bodySmall = bodySmall.copy(fontFamily = Jakarta),
        labelLarge = labelLarge.copy(fontFamily = Jakarta, fontWeight = FontWeight.SemiBold),
        labelMedium = labelMedium.copy(fontFamily = Jakarta, fontWeight = FontWeight.Medium),
        labelSmall = labelSmall.copy(fontFamily = Jakarta, fontWeight = FontWeight.Medium),
    )
}
