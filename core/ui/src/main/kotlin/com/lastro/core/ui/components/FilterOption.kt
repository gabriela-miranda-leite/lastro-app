package com.lastro.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroFilterOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val borderColor = if (selected) accentColor else MaterialTheme.colorScheme.outline

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = LastroSpacing.minTouchTarget)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.RadioButton
                this.selected = selected
            }
            .padding(vertical = LastroSpacing.listItemVertical),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .border(2.dp, borderColor, CircleShape)
                .drawBehind {
                    if (selected) drawCircle(accentColor)
                },
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .drawBehind { drawCircle(androidx.compose.ui.graphics.Color.White) },
                )
            }
        }
        Spacer(modifier = Modifier.width(LastroSpacing.md))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(name = "FilterOption selecionado - Dark")
@Composable
private fun PreviewSelected() = LastroTheme { LastroFilterOption("Todas", selected = true, onClick = {}) }

@Preview(name = "FilterOption - Dark")
@Composable
private fun PreviewUnselected() = LastroTheme { LastroFilterOption("Despesas", selected = false, onClick = {}) }

@Preview(name = "FilterOption - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroFilterOption("Receitas", selected = true, onClick = {}) }
