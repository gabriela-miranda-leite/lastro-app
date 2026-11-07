package com.lastro.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroAmountDisplay(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onBackground,
    hidden: Boolean = false,
    onToggleVisibility: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics {
                contentDescription = if (hidden) "$label: oculto" else "$label: $value"
            },
        ) {
            Text(
                text = if (hidden) "••••••" else value,
                style = MaterialTheme.typography.displayLarge,
                color = if (hidden) MaterialTheme.colorScheme.onSurfaceVariant else valueColor,
                letterSpacing = if (hidden) androidx.compose.ui.unit.TextUnit.Unspecified
                                else MaterialTheme.typography.displayLarge.letterSpacing,
            )
            if (onToggleVisibility != null) {
                Spacer(modifier = Modifier.width(LastroSpacing.md))
                Icon(
                    painter = painterResource(if (hidden) LastroIcons.EyeOff else LastroIcons.Eye),
                    contentDescription = if (hidden) "Mostrar valor" else "Ocultar valor",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable(onClick = onToggleVisibility)
                        .width(LastroIconSize.sm),
                )
            }
        }
    }
}

@Preview(name = "AmountDisplay visível - Dark")
@Composable
private fun PreviewVisible() = LastroTheme {
    LastroAmountDisplay(label = "Saldo total", value = "R$ 4.820,50", onToggleVisibility = {})
}

@Preview(name = "AmountDisplay oculto - Dark")
@Composable
private fun PreviewHidden() = LastroTheme {
    LastroAmountDisplay(label = "Saldo total", value = "R$ 4.820,50", hidden = true, onToggleVisibility = {})
}

@Preview(name = "AmountDisplay positivo - Light")
@Composable
private fun PreviewPositive() = LastroTheme(darkTheme = false) {
    LastroAmountDisplay(label = "Receitas", value = "R$ 6.500", valueColor = LastroColors.Positive)
}

@Preview(name = "AmountDisplay negativo - Light")
@Composable
private fun PreviewNegative() = LastroTheme(darkTheme = false) {
    LastroAmountDisplay(label = "Despesas", value = "R$ 3.431", valueColor = LastroColors.Negative)
}
