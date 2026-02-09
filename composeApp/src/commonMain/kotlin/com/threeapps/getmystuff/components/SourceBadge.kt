package com.threeapps.getmystuff.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.threeapps.getmystuff.theme.AppColors

@Composable
fun SourceBadge(
    source: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (source.lowercase()) {
        "willhaben" -> AppColors.Willhaben to Color.White
        "amazon" -> AppColors.Amazon to Color.Black
        "geizhals" -> AppColors.Geizhals to Color.White
        "idealo" -> AppColors.Idealo to Color.White
        "refurbed" -> AppColors.Refurbed to Color.White
        "ebay" -> AppColors.Ebay to Color.White
        else -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    }

    Text(
        text = source.replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
