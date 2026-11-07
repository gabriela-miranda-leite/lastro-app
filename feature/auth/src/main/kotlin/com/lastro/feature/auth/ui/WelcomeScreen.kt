package com.lastro.feature.auth.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.R as CoreR
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.R

@Composable
fun WelcomeScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    WelcomeScreenContent(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToLogin = onNavigateToLogin,
    )
}

@Composable
internal fun WelcomeScreenContent(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val accentGradient = Brush.linearGradient(
        listOf(LastroColors.Accent, LastroColors.AccentSecondary)
    )

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = LastroSpacing.screenHorizontal)
                    .padding(top = LastroSpacing.md, bottom = LastroSpacing.bottomSafe),
            ) {
                LastroButton(
                    text = stringResource(R.string.welcome_action_register),
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(LastroSpacing.sm))
                LastroButton(
                    text = buildString {
                        append(stringResource(R.string.welcome_action_login_prefix))
                        append(" ")
                        append(stringResource(R.string.welcome_action_login))
                    },
                    onClick = onNavigateToLogin,
                    variant = ButtonVariant.Ghost,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.sectionTopLarge))

            Box(
                modifier = Modifier
                    .size(LastroIconWrapSize.xl)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(accentGradient),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_welcome_hero),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.welcome_title_1))
                    append("\n")
                    append(stringResource(R.string.welcome_title_2))
                    append(" ")
                    withStyle(SpanStyle(color = LastroColors.Accent)) {
                        append(stringResource(R.string.welcome_title_accent))
                    }
                },
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Column(verticalArrangement = Arrangement.spacedBy(LastroSpacing.md)) {
                WelcomeFeatureCard(
                    iconRes = CoreR.drawable.ic_trending_up,
                    iconTint = LastroColors.Accent,
                    iconBackground = LastroColors.Accent.copy(alpha = 0.15f),
                    title = stringResource(R.string.welcome_feature1_title),
                    description = stringResource(R.string.welcome_feature1_desc),
                )
                WelcomeFeatureCard(
                    iconRes = CoreR.drawable.ic_users,
                    iconTint = LastroColors.Positive,
                    iconBackground = LastroColors.Positive.copy(alpha = 0.15f),
                    title = stringResource(R.string.welcome_feature2_title),
                    description = stringResource(R.string.welcome_feature2_desc),
                )
                WelcomeFeatureCard(
                    iconRes = CoreR.drawable.ic_calendar,
                    iconTint = LastroColors.Warning,
                    iconBackground = LastroColors.Warning.copy(alpha = 0.15f),
                    title = stringResource(R.string.welcome_feature3_title),
                    description = stringResource(R.string.welcome_feature3_desc),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))
        }
    }
}

@Composable
private fun WelcomeFeatureCard(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    iconBackground: Color,
    title: String,
    description: String,
    iconSize: Dp = 22.dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraLarge)
            .padding(horizontal = LastroSpacing.md, vertical = LastroSpacing.xl),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(LastroSpacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(LastroIconWrapSize.sm)
                .clip(MaterialTheme.shapes.medium)
                .background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(iconSize),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(name = "Welcome - Dark")
@Composable
private fun WelcomePreview() = LastroTheme(darkTheme = true) {
    WelcomeScreenContent(onNavigateToRegister = {}, onNavigateToLogin = {})
}
