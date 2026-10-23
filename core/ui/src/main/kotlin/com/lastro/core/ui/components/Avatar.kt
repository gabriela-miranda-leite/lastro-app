package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroAvatar(
    initial: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    brush: Brush = Brush.linearGradient(listOf(LastroColors.Accent, LastroColors.AccentSecondary)),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(brush),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.4f).sp,
        )
    }
}

@Composable
fun LastroAvatarOverlap(
    initials: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    val brushes = listOf(
        Brush.linearGradient(listOf(LastroColors.Accent, LastroColors.AccentSecondary)),
        Brush.linearGradient(listOf(LastroColors.Positive, Color(0xFF2DB89A))),
        Brush.linearGradient(listOf(Color(0xFFFF7C6B), Color(0xFFFFD06B))),
    )
    Box(modifier = modifier.size(size + (initials.size - 1) * (size * 0.4f))) {
        initials.forEachIndexed { index, initial ->
            LastroAvatar(
                initial = initial,
                size = size,
                brush = brushes[index % brushes.size],
                modifier = Modifier
                    .offset(x = (size * 0.4f * index))
                    .border(3.dp, MaterialTheme.colorScheme.background, CircleShape),
            )
        }
    }
}

@Preview(name = "Avatar - Dark")
@Composable
private fun PreviewDark() = LastroTheme { LastroAvatar(initial = "G") }

@Preview(name = "AvatarOverlap - Dark")
@Composable
private fun PreviewOverlap() = LastroTheme {
    LastroAvatarOverlap(initials = listOf("G", "M"))
}

@Preview(name = "Avatar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroAvatar(initial = "M", size = 60.dp) }
