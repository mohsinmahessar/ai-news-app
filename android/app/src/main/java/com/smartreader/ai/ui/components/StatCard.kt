package com.smartreader.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartreader.ai.ui.theme.brandGradient

/** Analytics stat tile: icon chip + big value + label. Optionally gradient-filled. */
@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (highlighted) 0.dp else 1.dp),
    ) {
        Column(
            modifier = if (highlighted) Modifier.background(brandGradient()).padding(18.dp)
            else Modifier.padding(18.dp),
        ) {
            val accent = if (highlighted) Color.White else MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (highlighted) Color.White.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accent)
            }
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (highlighted) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 12.dp),
            )
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (highlighted) Color.White.copy(alpha = 0.85f)
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
