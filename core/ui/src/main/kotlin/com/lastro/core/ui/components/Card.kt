package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroCard(
    modifier: Modifier = Modifier,
    padding: Dp = LastroSpacing.xl,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(padding), content = content)
    }
}

@Preview(name = "Card - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroCard { Text("Conteúdo do card") }
}

@Preview(name = "Card - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroCard { Text("Conteúdo do card") }
}
