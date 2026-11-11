package com.lastro.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class FeedbackScreenVariant { Success, Error, Info, Warning }

@Composable
fun LastroFeedbackScreen(
    title: String,
    description: String,
    variant: FeedbackScreenVariant,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    primaryActionVariant: ButtonVariant = ButtonVariant.Primary,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val (iconDefault, iconTint, iconBg) = when (variant) {
        FeedbackScreenVariant.Success -> Triple(
            LastroIcons.CheckCircle,
            LastroColors.Positive,
            LastroColors.Positive.copy(alpha = 0.12f),
        )
        FeedbackScreenVariant.Error -> Triple(
            LastroIcons.AlertCircle,
            LastroColors.Negative,
            LastroColors.Negative.copy(alpha = 0.12f),
        )
        FeedbackScreenVariant.Warning -> Triple(
            LastroIcons.AlertTriangle,
            LastroColors.Warning,
            LastroColors.Warning.copy(alpha = 0.12f),
        )
        FeedbackScreenVariant.Info -> Triple(
            LastroIcons.InfoCircle,
            LastroColors.Accent,
            LastroColors.Accent.copy(alpha = 0.12f),
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (primaryActionLabel != null || secondaryActionLabel != null || footer != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LastroSpacing.screenHorizontal)
                            .padding(top = LastroSpacing.md, bottom = LastroSpacing.bottomSafe),
                    ) {
                        if (primaryActionLabel != null && onPrimaryAction != null) {
                            LastroButton(
                                text = primaryActionLabel,
                                onClick = onPrimaryAction,
                                variant = primaryActionVariant,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        if (secondaryActionLabel != null && onSecondaryAction != null) {
                            Spacer(modifier = Modifier.height(LastroSpacing.sm))
                            LastroButton(
                                text = secondaryActionLabel,
                                onClick = onSecondaryAction,
                                variant = ButtonVariant.Ghost,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        if (footer != null) {
                            Spacer(modifier = Modifier.height(LastroSpacing.md))
                            footer()
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(LastroIconWrapSize.xl)
                    .background(iconBg, MaterialTheme.shapes.extraLarge),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes ?: iconDefault),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(LastroIconSize.xl),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}

@Preview(name = "FeedbackScreen Success - Dark")
@Composable
private fun PreviewSuccessDark() = LastroTheme {
    LastroFeedbackScreen(
        title = "Conta criada com sucesso!",
        description = "Bem-vindo ao Lastro. Seu perfil financeiro está pronto.",
        variant = FeedbackScreenVariant.Success,
        primaryActionLabel = "Começar",
        onPrimaryAction = {},
    )
}

@Preview(name = "FeedbackScreen Error - Dark")
@Composable
private fun PreviewErrorDark() = LastroTheme {
    LastroFeedbackScreen(
        title = "Algo deu errado",
        description = "Não foi possível processar sua solicitação. Tente novamente.",
        variant = FeedbackScreenVariant.Error,
        primaryActionLabel = "Tentar novamente",
        onPrimaryAction = {},
        secondaryActionLabel = "Voltar para o início",
        onSecondaryAction = {},
    )
}

@Preview(name = "FeedbackScreen Info - Light")
@Composable
private fun PreviewInfoLight() = LastroTheme(darkTheme = false) {
    LastroFeedbackScreen(
        title = "Verificação necessária",
        description = "Enviamos um e-mail para confirmar sua identidade antes de continuar.",
        variant = FeedbackScreenVariant.Info,
        primaryActionLabel = "Entendi",
        onPrimaryAction = {},
    )
}
