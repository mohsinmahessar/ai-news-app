package com.smartreader.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartreader.ai.ui.theme.brandGradient

/**
 * Premium blue gradient hero used at the top of screens (e.g. Home).
 * Optional [content] renders below the title/subtitle inside the gradient.
 */
@Composable
fun GradientHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(brandGradient())
            .padding(start = 20.dp, end = 20.dp, top = 56.dp, bottom = 22.dp),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
        )
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        content?.invoke(this)
    }
}
