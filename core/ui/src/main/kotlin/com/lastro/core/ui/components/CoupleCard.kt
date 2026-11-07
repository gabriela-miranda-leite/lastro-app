package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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

@Composable
fun LastroCoupleInviteCard(
    inviteCode: String,
    onShareCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraLarge)
            .padding(LastroSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(LastroIconWrapSize.sm)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(LastroColors.Accent, LastroColors.AccentSecondary))
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(LastroIcons.User),
                    contentDescription = null,
                    tint = LastroColors.AccentText,
                    modifier = Modifier.size(LastroIconSize.sm),
                )
            }
            Box(
                modifier = Modifier
                    .size(LastroIconWrapSize.sm)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(LastroIcons.Plus),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(LastroIconSize.sm),
                )
            }
        }
        Spacer(modifier = Modifier.height(LastroSpacing.md))
        Text(
            text = "Convide seu parceiro",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xs))
        Text(
            text = "Compartilhe o código abaixo para conectar finanças juntos.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                .clickable(onClick = onShareCode)
                .padding(horizontal = LastroSpacing.xl, vertical = LastroSpacing.md)
                .semantics { role = Role.Button },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = inviteCode,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified,
                color = LastroColors.Accent,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(LastroSpacing.sm))
            Icon(
                painter = painterResource(LastroIcons.Link),
                contentDescription = "Copiar código",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(LastroIconSize.sm),
            )
        }
    }
}

data class CoupleStatTile(val label: String, val value: String, val valueColor: Color? = null)

@Composable
fun LastroCoupleConnectedCard(
    myName: String,
    partnerName: String,
    subtitle: String? = null,
    stats: List<CoupleStatTile> = emptyList(),
    modifier: Modifier = Modifier,
    onViewDetails: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(
                        LastroColors.Positive.copy(alpha = 0.08f),
                        LastroColors.Accent.copy(alpha = 0.06f),
                    )
                ),
                MaterialTheme.shapes.extraLarge,
            )
            .border(1.dp, LastroColors.Positive.copy(alpha = 0.25f), MaterialTheme.shapes.extraLarge)
            .then(if (onViewDetails != null) Modifier.clickable(onClick = onViewDetails) else Modifier)
            .padding(LastroSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LastroAvatar(initial = myName)
            Box(
                modifier = Modifier
                    .width(LastroSpacing.xxl)
                    .height(2.dp)
                    .background(
                        Brush.linearGradient(listOf(LastroColors.Accent, LastroColors.Positive))
                    ),
            )
            LastroAvatar(
                initial = partnerName,
                brush = Brush.linearGradient(listOf(LastroColors.Positive, Color(0xFF2DB89A))),
            )
        }
        Spacer(modifier = Modifier.height(LastroSpacing.md))
        Text(
            text = "$myName & $partnerName",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (stats.isNotEmpty()) {
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LastroSpacing.sm),
            ) {
                stats.forEach { stat ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                            .padding(LastroSpacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stat.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(LastroSpacing.xs))
                        Text(
                            text = stat.value,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = stat.valueColor ?: MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "CoupleInvite - Dark")
@Composable
private fun PreviewInviteDark() = LastroTheme {
    LastroCoupleInviteCard(
        inviteCode = "LASTRO-7X2K",
        onShareCode = {},
        modifier = Modifier.padding(LastroSpacing.md),
    )
}

@Preview(name = "CoupleConnected - Dark")
@Composable
private fun PreviewConnectedDark() = LastroTheme {
    LastroCoupleConnectedCard(
        myName = "G",
        partnerName = "M",
        subtitle = "Casal conectado desde set. 2026",
        stats = listOf(
            CoupleStatTile(label = "Metas juntos", value = "3"),
            CoupleStatTile(label = "Este mês", value = "R$ 13k", valueColor = LastroColors.Positive),
        ),
        onViewDetails = {},
        modifier = Modifier.padding(LastroSpacing.md),
    )
}

@Preview(name = "CoupleConnected - Light")
@Composable
private fun PreviewConnectedLight() = LastroTheme(darkTheme = false) {
    LastroCoupleConnectedCard(
        myName = "G",
        partnerName = "M",
        subtitle = "Casal desde set. 2026",
        stats = listOf(
            CoupleStatTile(label = "Metas juntos", value = "3"),
            CoupleStatTile(label = "Este mês", value = "R$ 13k", valueColor = LastroColors.Positive),
        ),
        modifier = Modifier.padding(LastroSpacing.md),
    )
}
