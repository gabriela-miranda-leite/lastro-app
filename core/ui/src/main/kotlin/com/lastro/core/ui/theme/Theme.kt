package com.lastro.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = LastroColors.Background,
    surface = LastroColors.Surface,
    surfaceVariant = LastroColors.Surface2,
    outline = LastroColors.Border,
    onBackground = LastroColors.TextPrimary,
    onSurface = LastroColors.TextPrimary,
    onSurfaceVariant = LastroColors.TextOnSurface,
    primary = LastroColors.Accent,
    onPrimary = LastroColors.AccentText,
    secondary = LastroColors.AccentSecondary,
    onSecondary = LastroColors.AccentText,
    error = LastroColors.Negative,
    onError = LastroColors.AccentText,
    tertiary = LastroColors.Positive,
)

private val LightColorScheme = lightColorScheme(
    background = LastroColors.BackgroundLight,
    surface = LastroColors.SurfaceLight,
    surfaceVariant = LastroColors.Surface2Light,
    outline = LastroColors.BorderLight,
    onBackground = LastroColors.TextPrimaryLight,
    onSurface = LastroColors.TextPrimaryLight,
    onSurfaceVariant = LastroColors.TextOnSurfaceLight,
    primary = LastroColors.Accent,
    onPrimary = LastroColors.AccentText,
    secondary = LastroColors.AccentSecondary,
    onSecondary = LastroColors.AccentText,
    error = LastroColors.Negative,
    onError = LastroColors.AccentText,
    tertiary = LastroColors.Positive,
)

@Composable
fun LastroTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LastroTypography,
        shapes = LastroShapes,
        content = content,
    )
}
