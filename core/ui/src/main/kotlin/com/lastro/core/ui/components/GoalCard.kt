package com.lastro.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroGoalCard(
    name: String,
    deadline: String,
    currentValue: String,
    targetValue: String,
    progress: Float,
    iconRes: Int,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null,
) {
    val pct = (progress * 100).toInt()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .semantics { contentDescription = "Meta $name: $pct% concluído" },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(LastroSpacing.xl)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LastroIconWrap(
                    painter = painterResource(iconRes),
                    variant = IconWrapVariant.Default,
                    wrapSize = LastroIconWrapSize.sm,
                    iconSize = LastroIconSize.sm,
                )
                Spacer(modifier = Modifier.width(LastroSpacing.md))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = deadline,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    painter = painterResource(LastroIcons.ChevronRight),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(LastroIconSize.xs),
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.md))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = currentValue,
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "de $targetValue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = accentColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "$pct% concluído",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Preview(name = "GoalCard 23% - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroGoalCard(
        name = "Casa própria",
        deadline = "Meta: dez 2027",
        currentValue = "R$ 18.400",
        targetValue = "R$ 80.000",
        progress = 0.23f,
        iconRes = LastroIcons.Home,
        onClick = {},
    )
}

@Preview(name = "GoalCard 61% - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroGoalCard(
        name = "Viagem Europa",
        deadline = "Meta: jun 2027",
        currentValue = "R$ 9.200",
        targetValue = "R$ 15.000",
        progress = 0.61f,
        iconRes = LastroIcons.TrendingUp,
        accentColor = LastroColors.Positive,
        onClick = {},
    )
}
