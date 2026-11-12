package com.lastro.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

data class ChartPoint(val label: String, val value: Float)
data class ChartBar(val label: String, val value: Float, val color: Color)
data class DonutSlice(val label: String, val value: Float, val color: Color)
data class ChartAxisLabel(val index: Int, val text: String)

@Composable
private fun ChartCardContainer(
    title: String,
    subtitle: String? = null,
    titleTrailing: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraLarge)
            .padding(LastroSpacing.xl),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (titleTrailing != null) {
                Text(
                    text = titleTrailing,
                    style = MaterialTheme.typography.labelMedium,
                    color = LastroColors.Accent,
                )
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        content()
    }
}

@Composable
fun LastroLineChartCard(
    title: String,
    points: List<ChartPoint>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    lineColor: Color = LastroColors.Accent,
    chartHeight: Dp = 100.dp,
    highlightIndex: Int? = points.lastIndex.takeIf { points.isNotEmpty() },
    axisLabels: List<ChartAxisLabel>? = null,
) {
    ChartCardContainer(title = title, subtitle = subtitle, modifier = modifier) {
        if (points.size >= 2) {
            val maxVal = points.maxOf { it.value }.coerceAtLeast(1f)
            val minVal = points.minOf { it.value }.coerceAtMost(0f)
            val range = (maxVal - minVal).coerceAtLeast(1f)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight),
            ) {
                val w = size.width
                val h = size.height
                val stepX = w / (points.size - 1).coerceAtLeast(1)

                fun yOf(value: Float) = h - ((value - minVal) / range) * h * 0.9f - h * 0.05f

                val path = Path()
                val fillPath = Path()

                points.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val y = yOf(pt.value)
                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, h)
                        fillPath.lineTo(x, y)
                    } else {
                        path.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }
                fillPath.lineTo((points.size - 1) * stepX, h)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(lineColor.copy(alpha = 0.18f), Color.Transparent),
                    ),
                )
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                )

                val hlIndex = (highlightIndex ?: (points.size - 1)).coerceIn(0, points.size - 1)
                val hlX = hlIndex * stepX
                val hlY = yOf(points[hlIndex].value)
                drawCircle(color = lineColor.copy(alpha = 0.2f), radius = 7.dp.toPx(), center = Offset(hlX, hlY))
                drawCircle(color = lineColor, radius = 4.dp.toPx(), center = Offset(hlX, hlY))
            }
        }
        Spacer(modifier = Modifier.height(LastroSpacing.sm))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val labels = axisLabels ?: listOfNotNull(
                points.firstOrNull()?.let { ChartAxisLabel(0, it.label) },
                points.lastOrNull()?.let { ChartAxisLabel(points.lastIndex, it.label) },
            )
            labels.forEach { axisLabel ->
                val isHighlight = axisLabel.index == highlightIndex
                Text(
                    text = axisLabel.text,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isHighlight) FontWeight.SemiBold else null,
                    color = if (isHighlight) lineColor else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun LastroBarChartCard(
    title: String,
    bars: List<ChartBar>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    chartHeight: Dp = 100.dp,
) {
    ChartCardContainer(title = title, subtitle = subtitle, modifier = modifier) {
        val maxVal = bars.maxOf { it.value }.coerceAtLeast(1f)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(LastroSpacing.xs),
        ) {
            bars.forEach { bar ->
                val fraction = (bar.value / maxVal).coerceIn(0.05f, 1f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(chartHeight * fraction)
                        .background(bar.color, MaterialTheme.shapes.small),
                )
            }
        }
        Spacer(modifier = Modifier.height(LastroSpacing.sm))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            bars.forEach { bar ->
                Text(
                    text = bar.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun LastroDonutChartCard(
    title: String,
    slices: List<DonutSlice>,
    modifier: Modifier = Modifier,
    titleTrailing: String? = null,
    centerLabel: String? = null,
    donutSize: Dp = 120.dp,
) {
    ChartCardContainer(title = title, titleTrailing = titleTrailing, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LastroSpacing.xl),
        ) {
            val total = slices.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(1f)
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(donutSize)) {
                    val strokeWidth = 20.dp.toPx()
                    var startAngle = -90f
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    val arcSize = Size(diameter, diameter)

                    slices.forEach { slice ->
                        val sweep = (slice.value / total) * 360f
                        drawArc(
                            color = slice.color,
                            startAngle = startAngle,
                            sweepAngle = sweep - 2f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        )
                        startAngle += sweep
                    }
                }
                if (centerLabel != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "total",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = centerLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(LastroSpacing.sm)) {
                slices.forEach { slice ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(slice.color, MaterialTheme.shapes.extraLarge),
                        )
                        Spacer(modifier = Modifier.width(LastroSpacing.xs))
                        Text(
                            text = slice.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "${((slice.value / total) * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "LineChart - Dark")
@Composable
private fun PreviewLineDark() = LastroTheme {
    LastroLineChartCard(
        title = "SALDO DO MÊS",
        subtitle = "R$ 3.240,00",
        points = listOf(
            ChartPoint("1 out", 1200f), ChartPoint("8 out", 2100f),
            ChartPoint("15 out", 1800f), ChartPoint("22 out", 2600f),
            ChartPoint("30 out", 3240f),
        ),
        modifier = Modifier.padding(LastroSpacing.md),
    )
}

@Preview(name = "BarChart - Dark")
@Composable
private fun PreviewBarDark() = LastroTheme {
    LastroBarChartCard(
        title = "GASTOS POR CATEGORIA",
        bars = listOf(
            ChartBar("Mor.", 1200f, LastroColors.Accent),
            ChartBar("Ali.", 850f, LastroColors.Positive),
            ChartBar("Tra.", 420f, LastroColors.Warning),
            ChartBar("Laz.", 310f, LastroColors.Wishes),
            ChartBar("Saú.", 220f, LastroColors.Negative),
        ),
        modifier = Modifier.padding(LastroSpacing.md),
    )
}

@Preview(name = "DonutChart - Dark")
@Composable
private fun PreviewDonutDark() = LastroTheme {
    LastroDonutChartCard(
        title = "DISTRIBUIÇÃO",
        centerLabel = "100%",
        slices = listOf(
            DonutSlice("Moradia", 40f, LastroColors.Accent),
            DonutSlice("Alimentação", 25f, LastroColors.Positive),
            DonutSlice("Transporte", 15f, LastroColors.Warning),
            DonutSlice("Outros", 20f, LastroColors.TextDim),
        ),
        modifier = Modifier.padding(LastroSpacing.md),
    )
}
