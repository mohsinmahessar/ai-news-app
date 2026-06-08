package com.smartreader.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

/**
 * Auto-generated gradient book cover — no image assets needed.
 *
 * A deterministic blue-family gradient is derived from the title's hashCode
 * (stable across runs), with the title's initials in white. This gives every
 * book a distinct, premium-looking cover even though PDFs have no cover art.
 */
@Composable
fun BookCover(
    title: String,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 10,
) {
    val brush = remember(title) { coverBrush(title) }
    val initials = remember(title) { title.toInitials() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(brush),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge,
        )
        Icon(
            Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.22f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .size(18.dp),
        )
    }
}

private fun coverBrush(title: String): Brush {
    // Spread hue within the blue/indigo family (~200–250°) for variety on-brand.
    val seed = title.hashCode().absoluteValue
    val hue = 200f + (seed % 50)
    val top = Color.hsl(hue, 0.62f, 0.60f)
    val bottom = Color.hsl((hue + 14f).coerceAtMost(255f), 0.66f, 0.42f)
    return Brush.linearGradient(listOf(top, bottom))
}

private fun String.toInitials(): String =
    trim().split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "?" }
