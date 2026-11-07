package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class StatusBadgeVariant { Paid, Pending, Overdue, Cancelled }

@Composable
fun LastroStatusBadge(
    variant: StatusBadgeVariant,
    modifier: Modifier = Modifier,
    customLabel: String? = null,
) {
    val (label, fg, bg) = when (variant) {
        StatusBadgeVariant.Paid -> Triple(
            customLabel ?: "Pago",
            LastroColors.Positive,
            LastroColors.Positive.copy(alpha = 0.12f),
        )
        StatusBadgeVariant.Pending -> Triple(
            customLabel ?: "Pendente",
            LastroColors.Warning,
            LastroColors.Warning.copy(alpha = 0.12f),
        )
        StatusBadgeVariant.Overdue -> Triple(
            customLabel ?: "Atrasado",
            LastroColors.Negative,
            LastroColors.Negative.copy(alpha = 0.12f),
        )
        StatusBadgeVariant.Cancelled -> Triple(
            customLabel ?: "Cancelado",
            MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.surfaceVariant,
        )
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = fg,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(bg)
            .border(1.dp, fg.copy(alpha = 0.25f), MaterialTheme.shapes.extraLarge)
            .padding(horizontal = LastroSpacing.sm, vertical = LastroSpacing.xs),
    )
}

@Preview(name = "StatusBadge - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    Row(modifier = Modifier.padding(LastroSpacing.md)) {
        LastroStatusBadge(variant = StatusBadgeVariant.Paid)
        Box(modifier = Modifier.padding(horizontal = LastroSpacing.xs))
        LastroStatusBadge(variant = StatusBadgeVariant.Pending)
        Box(modifier = Modifier.padding(horizontal = LastroSpacing.xs))
        LastroStatusBadge(variant = StatusBadgeVariant.Overdue)
        Box(modifier = Modifier.padding(horizontal = LastroSpacing.xs))
        LastroStatusBadge(variant = StatusBadgeVariant.Cancelled)
    }
}

@Preview(name = "StatusBadge - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    Row(modifier = Modifier.padding(LastroSpacing.md)) {
        LastroStatusBadge(variant = StatusBadgeVariant.Paid)
        Box(modifier = Modifier.padding(horizontal = LastroSpacing.xs))
        LastroStatusBadge(variant = StatusBadgeVariant.Overdue)
    }
}
