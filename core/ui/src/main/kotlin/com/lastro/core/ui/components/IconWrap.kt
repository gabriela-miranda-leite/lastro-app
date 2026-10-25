package com.lastro.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroTheme

enum class IconWrapVariant(val color: Color) {
    Default(LastroColors.Accent),
    Positive(LastroColors.Positive),
    Negative(LastroColors.Negative),
}

private val IconWrapShape = RoundedCornerShape(20.dp)

@Composable
fun LastroIconWrap(
    icon: ImageVector,
    contentDescription: String? = null,
    variant: IconWrapVariant = IconWrapVariant.Default,
    modifier: Modifier = Modifier,
) {
    val bg = variant.color.copy(alpha = 0.12f)
    val border = variant.color.copy(alpha = 0.25f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
            .clip(IconWrapShape)
            .drawBehind { drawRect(bg) }
            .border(1.dp, border, IconWrapShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = variant.color,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Preview(name = "Default")
@Composable
private fun PreviewDefault() = LastroTheme {
    LastroIconWrap(icon = Icons.Outlined.AccountCircle)
}

@Preview(name = "Positive")
@Composable
private fun PreviewPositive() = LastroTheme {
    LastroIconWrap(icon = Icons.Outlined.AccountCircle, variant = IconWrapVariant.Positive)
}

@Preview(name = "Negative")
@Composable
private fun PreviewNegative() = LastroTheme {
    LastroIconWrap(icon = Icons.Outlined.AccountCircle, variant = IconWrapVariant.Negative)
}
