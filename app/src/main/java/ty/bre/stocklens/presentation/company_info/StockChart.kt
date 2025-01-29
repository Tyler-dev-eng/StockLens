package ty.bre.stocklens.presentation.company_info

import android.graphics.Color.WHITE
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ty.bre.stocklens.domain.model.IntradayInfoModel
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    infos: List<IntradayInfoModel> = emptyList(),
    modifier: Modifier = Modifier,
    graphColour: Color = MaterialTheme.colorScheme.primary
) {
    val spacing = 100f
    val transparentGraphColour = remember { graphColour.copy(alpha = 0.5f) }
    val upperValue =
        remember(infos) { (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember(infos) { (infos.minOfOrNull { it.close }?.toInt()) ?: 0 }

    val density = LocalDensity.current
    val textPaint = remember {
        Paint().apply {
            color = WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = if (infos.isNotEmpty()) (size.width - spacing) / infos.size else 0f
        val priceStep = (upperValue - lowerValue) / 5f

        // Draw Gridlines
        val gridColor = Color.Gray.copy(alpha = 0.3f)
        val strokeWidth = 1.dp.toPx()

        // Draw Horizontal Gridlines (Y-axis)
        (0..4).forEach { i ->
            val y = size.height - spacing - i * size.height / 5f
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(spacing, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = strokeWidth
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    y,
                    textPaint
                )
            }
        }

        // Draw Vertical Gridlines (X-axis)
        if (infos.isNotEmpty()) {
            (0 until infos.size - 1 step 2).forEach { i ->
                val x = spacing + i * spacePerHour
                drawLine(
                    color = gridColor,
                    start = androidx.compose.ui.geometry.Offset(x, spacing),
                    end = androidx.compose.ui.geometry.Offset(x, size.height - spacing),
                    strokeWidth = strokeWidth
                )

                val info = infos[i]
                val hour = info.date.hour
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        hour.toString(),
                        x,
                        size.height - 50,
                        textPaint
                    )
                }
            }

            // Draw "Hours" label below the x-axis values
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "Hours",
                    size.width / 2f,
                    size.height - 5,
                    textPaint
                )
            }
        }

        if (infos.isNotEmpty()) {
            // Stock Chart Path
            var lastX = 0f
            val strokePath = Path().apply {
                val height = size.height
                for (i in infos.indices) {
                    val info = infos[i]
                    val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                    val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                    val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                    val x1 = spacing + i * spacePerHour
                    val y1 = height - spacing - (leftRatio * height).toFloat()

                    val x2 = spacing + (i + 1) * spacePerHour
                    val y2 = height - spacing - (rightRatio * height).toFloat()

                    if (i == 0) {
                        moveTo(x1, y1)
                    }
                    lastX = (x1 + x2) / 2f
                    quadraticTo(x1, y1, lastX, (y1 + y2) / 2f)
                }
            }

            val fillPath = android.graphics.Path(strokePath.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(lastX, size.height - spacing)
                    lineTo(spacing, size.height - spacing)
                    close()
                }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColour,
                        Color.Transparent
                    ),
                    endY = size.height - spacing
                )
            )

            drawPath(
                path = strokePath,
                color = graphColour,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        } else {
            // Display "No Data" text at the center of the chart
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "No Data Available",
                    size.width / 2f,
                    size.height / 2f,
                    textPaint
                )
            }
        }
    }
}