package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroCategoryChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val bg = color.copy(alpha = 0.12f)
    val border = color.copy(alpha = 0.30f)

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(bg)
            .border(1.dp, border, MaterialTheme.shapes.extraLarge)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(horizontal = LastroSpacing.md, vertical = LastroSpacing.xs)
            .semantics { role = Role.Button },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color),
        )
        Spacer(modifier = Modifier.width(LastroSpacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

@Preview(name = "CategoryChip - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    Row(modifier = Modifier.padding(LastroSpacing.md)) {
        LastroCategoryChip(label = "Moradia", color = LastroColors.Accent)
        Spacer(modifier = Modifier.width(LastroSpacing.sm))
        LastroCategoryChip(label = "Alimentação", color = LastroColors.Positive)
        Spacer(modifier = Modifier.width(LastroSpacing.sm))
        LastroCategoryChip(label = "Transporte", color = LastroColors.Warning)
    }
}

@Preview(name = "CategoryChip - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    Row(modifier = Modifier.padding(LastroSpacing.md)) {
        LastroCategoryChip(label = "Lazer", color = LastroColors.Wishes)
        Spacer(modifier = Modifier.width(LastroSpacing.sm))
        LastroCategoryChip(label = "Saúde", color = LastroColors.Negative)
    }
}
