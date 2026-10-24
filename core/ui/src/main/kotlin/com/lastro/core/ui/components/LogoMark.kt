package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    iconSize: Dp = size * 0.5f,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.25f))
            .background(
                Brush.linearGradient(
                    colors = listOf(LastroColors.Accent, LastroColors.AccentSecondary),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Diamond,
            contentDescription = "Lastro",
            tint = Color.White,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Preview(name = "LogoMark - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroLogoMark()
}

@Preview(name = "LogoMark - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroLogoMark()
}
