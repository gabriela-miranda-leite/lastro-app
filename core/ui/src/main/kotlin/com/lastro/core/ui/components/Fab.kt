package com.lastro.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroFab(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Add,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Preview(name = "Fab - Dark")
@Composable
private fun PreviewDark() = LastroTheme { LastroFab(onClick = {}, contentDescription = "Nova transação") }

@Preview(name = "Fab - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroFab(onClick = {}, contentDescription = "Nova transação") }
