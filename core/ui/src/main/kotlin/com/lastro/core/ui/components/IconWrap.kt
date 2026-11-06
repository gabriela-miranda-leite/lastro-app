package com.lastro.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroTheme

enum class IconWrapVariant { Default, Positive, Negative }

@Composable
private fun IconWrapVariant.color(): Color = when (this) {
    IconWrapVariant.Default  -> MaterialTheme.colorScheme.primary
    IconWrapVariant.Positive -> MaterialTheme.colorScheme.tertiary
    IconWrapVariant.Negative -> MaterialTheme.colorScheme.error
}

private val IconWrapShape = RoundedCornerShape(20.dp)

@Composable
fun LastroIconWrap(
    painter: Painter,
    contentDescription: String? = null,
    variant: IconWrapVariant = IconWrapVariant.Default,
    wrapSize: Dp = LastroIconWrapSize.xl,
    iconSize: Dp = LastroIconSize.lg,
    modifier: Modifier = Modifier,
) {
    val color = variant.color()
    val bg = color.copy(alpha = 0.12f)
    val borderColor = color.copy(alpha = 0.25f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(wrapSize)
            .clip(IconWrapShape)
            .drawBehind { drawRect(bg) }
            .border(1.dp, borderColor, IconWrapShape),
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun LastroIconWrap(
    icon: ImageVector,
    contentDescription: String? = null,
    variant: IconWrapVariant = IconWrapVariant.Default,
    wrapSize: Dp = LastroIconWrapSize.xl,
    iconSize: Dp = LastroIconSize.lg,
    modifier: Modifier = Modifier,
) = LastroIconWrap(rememberVectorPainter(icon), contentDescription, variant, wrapSize, iconSize, modifier)

@Preview(name = "Default — Dark")
@Composable
private fun PreviewDefault() = LastroTheme(darkTheme = true) {
    LastroIconWrap(painter = painterResource(LastroIcons.Heart))
}

@Preview(name = "Positive — Dark")
@Composable
private fun PreviewPositive() = LastroTheme(darkTheme = true) {
    LastroIconWrap(painter = painterResource(LastroIcons.CheckCircle), variant = IconWrapVariant.Positive)
}

@Preview(name = "Negative — Dark")
@Composable
private fun PreviewNegative() = LastroTheme(darkTheme = true) {
    LastroIconWrap(painter = painterResource(LastroIcons.AlertCircle), variant = IconWrapVariant.Negative)
}

@Preview(name = "Default — Light")
@Composable
private fun PreviewDefaultLight() = LastroTheme(darkTheme = false) {
    LastroIconWrap(painter = painterResource(LastroIcons.Heart))
}

@Preview(name = "Positive — Light")
@Composable
private fun PreviewPositiveLight() = LastroTheme(darkTheme = false) {
    LastroIconWrap(painter = painterResource(LastroIcons.CheckCircle), variant = IconWrapVariant.Positive)
}

@Preview(name = "Negative — Light")
@Composable
private fun PreviewNegativeLight() = LastroTheme(darkTheme = false) {
    LastroIconWrap(painter = painterResource(LastroIcons.AlertCircle), variant = IconWrapVariant.Negative)
}

@Preview(name = "Size sm — Light")
@Composable
private fun PreviewSizeSm() = LastroTheme(darkTheme = false) {
    LastroIconWrap(
        painter = painterResource(LastroIcons.Bell),
        wrapSize = LastroIconWrapSize.sm,
        iconSize = LastroIconSize.sm,
    )
}

@Preview(name = "Size md — Light")
@Composable
private fun PreviewSizeMd() = LastroTheme(darkTheme = false) {
    LastroIconWrap(
        painter = painterResource(LastroIcons.Bell),
        wrapSize = LastroIconWrapSize.md,
        iconSize = LastroIconSize.md,
    )
}

@Preview(name = "Size lg — Light")
@Composable
private fun PreviewSizeLg() = LastroTheme(darkTheme = false) {
    LastroIconWrap(
        painter = painterResource(LastroIcons.Bell),
        wrapSize = LastroIconWrapSize.lg,
        iconSize = LastroIconSize.xl,
    )
}
