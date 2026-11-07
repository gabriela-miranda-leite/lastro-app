package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class FeedbackCardVariant { Success, Error, Info }

@Composable
fun LastroFeedbackCard(
    title: String,
    description: String,
    variant: FeedbackCardVariant,
    modifier: Modifier = Modifier,
) {
    val (iconRes, tint, bgColor, borderColor) = when (variant) {
        FeedbackCardVariant.Success -> FeedbackCardStyle(
            iconRes = LastroIcons.CheckCircle,
            tint = MaterialTheme.colorScheme.tertiary,
            bgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f),
            borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
        )
        FeedbackCardVariant.Error -> FeedbackCardStyle(
            iconRes = LastroIcons.AlertCircle,
            tint = MaterialTheme.colorScheme.error,
            bgColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
            borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.25f),
        )
        FeedbackCardVariant.Info -> FeedbackCardStyle(
            iconRes = LastroIcons.InfoCircle,
            tint = MaterialTheme.colorScheme.primary,
            bgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = LastroSpacing.xl, vertical = LastroSpacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = tint.copy(alpha = 0.15f),
                modifier = Modifier.size(LastroIconWrapSize.md),
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .padding(LastroSpacing.md)
                        .size(LastroIconSize.md),
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.md))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = tint,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private data class FeedbackCardStyle(
    val iconRes: Int,
    val tint: androidx.compose.ui.graphics.Color,
    val bgColor: androidx.compose.ui.graphics.Color,
    val borderColor: androidx.compose.ui.graphics.Color,
)

@Preview(name = "FeedbackCard Success - Dark")
@Composable
private fun PreviewSuccess() = LastroTheme {
    LastroFeedbackCard("Tudo certo!", "Sua senha foi atualizada com sucesso.", FeedbackCardVariant.Success)
}

@Preview(name = "FeedbackCard Error - Dark")
@Composable
private fun PreviewError() = LastroTheme {
    LastroFeedbackCard("Link expirado", "Links de redefinição são de uso único e expiram em 1 hora.", FeedbackCardVariant.Error)
}

@Preview(name = "FeedbackCard Success - Light")
@Composable
private fun PreviewSuccessLight() = LastroTheme(darkTheme = false) {
    LastroFeedbackCard("Tudo certo!", "Transação registrada com sucesso.", FeedbackCardVariant.Success)
}
