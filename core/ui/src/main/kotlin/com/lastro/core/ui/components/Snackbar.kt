package com.lastro.core.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class SnackbarVariant { Success, Error, Info }

@Composable
fun LastroSnackbar(
    message: String,
    variant: SnackbarVariant = SnackbarVariant.Info,
    modifier: Modifier = Modifier,
    action: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val (iconRes, tint) = when (variant) {
        SnackbarVariant.Success -> LastroIcons.CheckCircle to MaterialTheme.colorScheme.tertiary
        SnackbarVariant.Error   -> LastroIcons.AlertCircle to MaterialTheme.colorScheme.error
        SnackbarVariant.Info    -> LastroIcons.InfoCircle  to MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = LastroSpacing.lg, vertical = LastroSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = tint.copy(alpha = 0.15f),
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.padding(LastroSpacing.xs).size(LastroIconSize.xs),
                )
            }
            Spacer(modifier = Modifier.width(LastroSpacing.md))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            if (action != null && onAction != null) {
                Spacer(modifier = Modifier.width(LastroSpacing.md))
                Text(
                    text = action,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(onClick = onAction)
                        .padding(start = LastroSpacing.xs),
                )
            }
        }
    }
}

@Preview(name = "Snackbar Sucesso - Dark")
@Composable
private fun PreviewSuccess() = LastroTheme {
    LastroSnackbar("Transação adicionada com sucesso.", SnackbarVariant.Success, action = "Desfazer", onAction = {})
}

@Preview(name = "Snackbar Erro - Dark")
@Composable
private fun PreviewError() = LastroTheme {
    LastroSnackbar("Sem conexão. Tentando novamente...", SnackbarVariant.Error)
}

@Preview(name = "Snackbar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroSnackbar("Dados sincronizados.", SnackbarVariant.Success)
}
