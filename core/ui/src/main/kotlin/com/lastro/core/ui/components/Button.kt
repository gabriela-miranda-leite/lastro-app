package com.lastro.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class ButtonVariant { Primary, Positive, Negative, Ghost }

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
        .height(LastroSpacing.buttonHeight)

    val (bgColor, shadowColor) = when (variant) {
        ButtonVariant.Primary  -> LastroColors.Accent    to LastroColors.Accent.copy(alpha = 0.35f)
        ButtonVariant.Positive -> LastroColors.Positive  to LastroColors.Positive.copy(alpha = 0.30f)
        ButtonVariant.Negative -> LastroColors.Negative  to LastroColors.Negative.copy(alpha = 0.30f)
        ButtonVariant.Ghost    -> Color.Transparent      to Color.Transparent
    }

    if (variant == ButtonVariant.Ghost) {
        Surface(
            modifier = baseModifier
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.large)
                .semantics { role = Role.Button },
            shape = MaterialTheme.shapes.large,
            color = Color.Transparent,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LastroSpacing.buttonHeight)
                    .clickable(enabled = enabled && !loading, onClick = onClick),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                )
            }
        }
    } else {
        Button(
            onClick = onClick,
            modifier = baseModifier.shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.large,
                ambientColor = shadowColor,
                spotColor = shadowColor,
            ),
            enabled = enabled && !loading,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = bgColor,
                contentColor = Color.White,
                disabledContainerColor = bgColor.copy(alpha = 0.4f),
                disabledContentColor = Color.White.copy(alpha = 0.4f),
            ),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp).semantics { contentDescription = "Carregando" },
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Primary - Dark")
@Composable
private fun PreviewPrimary() = LastroTheme { LastroButton("Continuar", onClick = {}) }

@Preview(name = "Positive - Dark")
@Composable
private fun PreviewPositive() = LastroTheme { LastroButton("Ir para o login", onClick = {}, variant = ButtonVariant.Positive) }

@Preview(name = "Negative - Dark")
@Composable
private fun PreviewNegative() = LastroTheme { LastroButton("Excluir conta", onClick = {}, variant = ButtonVariant.Negative) }

@Preview(name = "Ghost - Dark")
@Composable
private fun PreviewGhost() = LastroTheme { LastroButton("Cancelar", onClick = {}, variant = ButtonVariant.Ghost) }

@Preview(name = "Disabled - Dark")
@Composable
private fun PreviewDisabled() = LastroTheme { LastroButton("Enviar", onClick = {}, enabled = false) }

@Preview(name = "Loading - Dark")
@Composable
private fun PreviewLoading() = LastroTheme { LastroButton("Entrar", onClick = {}, loading = true) }

@Preview(name = "Primary - Light")
@Composable
private fun PreviewPrimaryLight() = LastroTheme(darkTheme = false) { LastroButton("Continuar", onClick = {}) }

@Preview(name = "Ghost - Light")
@Composable
private fun PreviewGhostLight() = LastroTheme(darkTheme = false) { LastroButton("Cancelar", onClick = {}, variant = ButtonVariant.Ghost) }
