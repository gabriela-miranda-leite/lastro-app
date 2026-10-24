package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    label: String? = null,
    value: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null || value != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (value != null) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color),
            )
        }
    }
}

@Preview(name = "ProgressBar - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroProgressBar(
        progress = 0.78f,
        label = "Necessidades 50%",
        value = "R$ 2.541 / R$ 3.250",
        color = LastroColors.Accent,
        modifier = Modifier.padding(LastroSpacing.xl),
    )
}

@Preview(name = "ProgressBar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroProgressBar(
        progress = 0.46f,
        label = "Desejos 30%",
        value = "R$ 890 / R$ 1.950",
        color = LastroColors.Negative,
        modifier = Modifier.padding(LastroSpacing.xl),
    )
}
