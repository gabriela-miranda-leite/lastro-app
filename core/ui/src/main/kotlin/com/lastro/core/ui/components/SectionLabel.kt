package com.lastro.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Preview(name = "SectionLabel - Dark")
@Composable
private fun PreviewDark() = LastroTheme { LastroSectionLabel("Suas transações") }

@Preview(name = "SectionLabel - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroSectionLabel("Suas metas") }
