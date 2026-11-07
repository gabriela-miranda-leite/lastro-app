package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroBudgetBar(
    label: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
    currentValue: String? = null,
    targetValue: String? = null,
) {
    val pct = (progress * 100).toInt()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "$label: $pct%" },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                modifier = Modifier.weight(1f),
            )
            if (currentValue != null && targetValue != null) {
                Text(
                    text = "$currentValue / $targetValue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "$pct%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(LastroSpacing.xs))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview(name = "BudgetBar 50/30/20 - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    Column(modifier = Modifier.padding(LastroSpacing.xl), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(LastroSpacing.md)) {
        LastroBudgetBar("Necessidades 50%", progress = 0.78f, color = LastroColors.Accent, currentValue = "R$ 2.541", targetValue = "R$ 3.250")
        LastroBudgetBar("Desejos 30%", progress = 0.46f, color = LastroColors.Wishes, currentValue = "R$ 890", targetValue = "R$ 1.950")
        LastroBudgetBar("Invest. / Metas 20%", progress = 1f, color = LastroColors.Positive, currentValue = "R$ 800", targetValue = "R$ 800")
    }
}

@Preview(name = "BudgetBar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroBudgetBar("Necessidades 50%", progress = 0.5f, color = LastroColors.Accent)
}
