package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index
                Button(
                    onClick = { onOptionSelected(index) },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) selectedColor else Color.Transparent,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    elevation = null,
                ) {
                    Text(option, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Preview(name = "Segmented - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroSegmentedControl(options = listOf("Você", "Casal"), selectedIndex = 0, onOptionSelected = {})
}

@Preview(name = "Segmented - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroSegmentedControl(options = listOf("Despesa", "Receita"), selectedIndex = 1, onOptionSelected = {})
}
