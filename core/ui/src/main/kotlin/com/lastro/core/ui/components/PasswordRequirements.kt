package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

data class PasswordRequirement(
    val label: String,
    val met: Boolean,
)

@Composable
fun LastroPasswordRequirements(
    requirements: List<PasswordRequirement>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = LastroSpacing.lg, vertical = LastroSpacing.md),
        ) {
            requirements.forEachIndexed { index, req ->
                if (index > 0) Spacer(modifier = Modifier.padding(top = LastroSpacing.xs))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (req.met) {
                        Icon(
                            painter = painterResource(LastroIcons.Check),
                            contentDescription = "Requisito atendido",
                            tint = LastroColors.Positive,
                            modifier = Modifier.size(LastroIconSize.xs),
                        )
                    } else {
                        Spacer(modifier = Modifier.size(LastroIconSize.xs))
                    }
                    Spacer(modifier = Modifier.width(LastroSpacing.sm))
                    Text(
                        text = req.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (req.met) LastroColors.Positive
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(name = "PasswordRequirements - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroPasswordRequirements(
        requirements = listOf(
            PasswordRequirement("Mínimo 8 caracteres", met = true),
            PasswordRequirement("Uma letra maiúscula", met = true),
            PasswordRequirement("Um número", met = false),
            PasswordRequirement("Um caractere especial", met = false),
        )
    )
}

@Preview(name = "PasswordRequirements - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroPasswordRequirements(
        requirements = listOf(
            PasswordRequirement("Mínimo 8 caracteres", met = true),
            PasswordRequirement("Uma letra maiúscula", met = false),
        )
    )
}
