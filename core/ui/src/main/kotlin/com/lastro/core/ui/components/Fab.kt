package com.lastro.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroFab(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int = LastroIcons.Plus,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
    ) {
        Icon(painter = painterResource(iconRes), contentDescription = contentDescription)
    }
}

@Preview(name = "Fab - Dark")
@Composable
private fun PreviewDark() = LastroTheme { LastroFab(onClick = {}, contentDescription = "Nova transação") }

@Preview(name = "Fab - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroFab(onClick = {}, contentDescription = "Nova transação") }
