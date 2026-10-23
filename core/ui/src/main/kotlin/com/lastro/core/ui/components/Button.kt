package com.lastro.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

enum class ButtonVariant { Primary, Outlined, Ghost }

@Composable
fun LastroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    val baseModifier = modifier
        .fillMaxWidth()
        .height(52.dp)

    when (variant) {
        ButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = baseModifier,
            enabled = enabled && !loading,
            shape = MaterialTheme.shapes.large,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text, style = MaterialTheme.typography.labelLarge)
            }
        }

        ButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = baseModifier,
            enabled = enabled && !loading,
            shape = MaterialTheme.shapes.large,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text, style = MaterialTheme.typography.labelLarge)
            }
        }

        ButtonVariant.Ghost -> TextButton(
            onClick = onClick,
            modifier = baseModifier,
            enabled = enabled && !loading,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        ) {
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(name = "Primary")
@Composable
private fun PreviewPrimary() = LastroTheme { LastroButton("Entrar", onClick = {}) }

@Preview(name = "Outlined")
@Composable
private fun PreviewOutlined() = LastroTheme { LastroButton("Criar conta", onClick = {}, variant = ButtonVariant.Outlined) }

@Preview(name = "Ghost")
@Composable
private fun PreviewGhost() = LastroTheme { LastroButton("Pular por agora", onClick = {}, variant = ButtonVariant.Ghost) }

@Preview(name = "Loading")
@Composable
private fun PreviewLoading() = LastroTheme { LastroButton("Entrar", onClick = {}, loading = true) }
