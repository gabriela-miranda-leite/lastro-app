package com.lastro.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

@Composable
private fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.surfaceVariant,
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim - 300f, y = 0f),
        end = Offset(x = translateAnim, y = 0f),
    )
}

@Composable
fun LastroSkeletonRow(
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(shimmerBrush())
            .semantics(mergeDescendants = true) {},
    ) {
        Box(modifier = Modifier.size(1.dp, height))
    }
}

@Composable
fun LastroSkeletonCircle(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(shimmerBrush())
            .semantics(mergeDescendants = true) {},
    )
}

@Composable
fun LastroSkeletonBlock(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(shimmerBrush())
            .semantics(mergeDescendants = true) {},
    ) {
        Box(modifier = Modifier.size(1.dp, height))
    }
}

@Preview(name = "Skeleton Row - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroSkeletonRow(modifier = Modifier.size(200.dp, 14.dp))
}

@Preview(name = "Skeleton Circle - Dark")
@Composable
private fun PreviewCircle() = LastroTheme {
    LastroSkeletonCircle(size = 40.dp)
}

@Preview(name = "Skeleton Block - Light")
@Composable
private fun PreviewBlock() = LastroTheme(darkTheme = false) {
    LastroSkeletonBlock(height = 48.dp, modifier = Modifier.size(300.dp, 48.dp))
}
