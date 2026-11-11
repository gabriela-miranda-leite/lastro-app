package com.lastro.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroCard(
    modifier: Modifier = Modifier,
    padding: Dp = LastroSpacing.xl,
    shape: Shape = MaterialTheme.shapes.large,
    border: BorderStroke? = null,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val isCheckable = checked != null && onCheckedChange != null

    Card(
        modifier = modifier.then(
            if (isCheckable) {
                Modifier
                    .defaultMinSize(minHeight = LastroSpacing.minTouchTarget)
                    .toggleable(
                        value = checked == true,
                        onValueChange = { onCheckedChange?.invoke(it) },
                        role = Role.Checkbox,
                    )
            } else {
                Modifier
            }
        ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = border,
    ) {
        if (isCheckable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(LastroSpacing.md),
            ) {
                CardCheckboxIndicator(checked = checked == true)
                Column { content() }
            }
        } else {
            Column(modifier = Modifier.padding(padding), content = content)
        }
    }
}

@Composable
private fun CardCheckboxIndicator(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (checked) LastroColors.Accent else Color.Transparent)
            .border(
                1.dp,
                if (checked) LastroColors.Accent else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(6.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Preview(name = "Card - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroCard { Text("Conteúdo do card") }
}

@Preview(name = "Card - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroCard { Text("Conteúdo do card") }
}

@Preview(name = "Card - Checkbox")
@Composable
private fun PreviewCheckbox() = LastroTheme(darkTheme = false) {
    LastroCard(
        padding = LastroSpacing.lg,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        checked = true,
        onCheckedChange = {},
    ) {
        Text("Concordo com os Termos de Uso e com a Política de Privacidade.")
    }
}
