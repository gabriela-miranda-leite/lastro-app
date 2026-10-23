package com.lastro.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val background = if (selected) selectedColor else Color.Transparent
    val contentColor = when {
        selected && selectedColor == LastroColors.Positive -> Color(0xFF111111)
        selected && selectedColor == Color(0xFF6BFF9E) -> Color(0xFF111111)
        selected && selectedColor == Color(0xFFFFD06B) -> Color(0xFF111111)
        selected -> Color.White
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .border(
                width = 1.5.dp,
                color = if (selected) selectedColor else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.extraLarge,
            )
            .clickable(onClick = onClick),
        color = background,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
            )
        }
    }
}

@Preview(name = "Chip selecionado - Dark")
@Composable
private fun PreviewSelected() = LastroTheme {
    LastroChip(label = "Necessidade", selected = true, onClick = {})
}

@Preview(name = "Chip não selecionado - Dark")
@Composable
private fun PreviewUnselected() = LastroTheme {
    LastroChip(label = "Lazer", selected = false, onClick = {})
}

@Preview(name = "Chip - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroChip(label = "Receita", selected = true, onClick = {}, selectedColor = LastroColors.Positive)
}
