package com.threeapps.getmystuff.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.threeapps.shared.models.PricePoint
import com.threeapps.shared.utils.CurrencyUtils

@Composable
fun PriceHistoryChart(
    priceHistory: List<PricePoint>,
    modifier: Modifier = Modifier
) {
    if (priceHistory.isEmpty()) return

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier) {
        // Min/Max labels
        val minPrice = priceHistory.minOf { it.price }
        val maxPrice = priceHistory.maxOf { it.price }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Max: ${CurrencyUtils.formatPrice(maxPrice)}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Min: ${CurrencyUtils.formatPrice(minPrice)}",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            if (priceHistory.size < 2) return@Canvas

            val width = size.width
            val height = size.height
            val priceRange = maxPrice - minPrice
            
            val path = Path()
            
            priceHistory.forEachIndexed { index, point ->
                val x = (index.toFloat() / (priceHistory.size - 1)) * width
                val y = if (priceRange > 0) {
                    height - ((point.price - minPrice) / priceRange * height).toFloat()
                } else {
                    height / 2
                }
                
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3f)
            )

            // Draw points
            priceHistory.forEachIndexed { index, point ->
                val x = (index.toFloat() / (priceHistory.size - 1)) * width
                val y = if (priceRange > 0) {
                    height - ((point.price - minPrice) / priceRange * height).toFloat()
                } else {
                    height / 2
                }
                
                drawCircle(
                    color = primaryColor,
                    radius = 6f,
                    center = Offset(x, y)
                )
            }
        }
    }
}
