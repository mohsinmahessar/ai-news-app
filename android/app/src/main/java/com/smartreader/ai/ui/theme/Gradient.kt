package com.smartreader.ai.ui.theme

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

/** The signature blue brand gradient, top-left to bottom-right. */
fun brandGradient(): Brush = Brush.linearGradient(
    colors = BrandGradient,
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
)

/** Vertical brand gradient for full-screen heroes (splash / login). */
fun brandGradientVertical(): Brush = Brush.verticalGradient(BrandGradient)

/** Paints a view with the brand gradient — used for header heroes. */
fun Modifier.brandHeaderBackground(): Modifier = this.background(brandGradient())
