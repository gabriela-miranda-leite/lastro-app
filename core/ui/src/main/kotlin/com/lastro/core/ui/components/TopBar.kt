package com.lastro.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lastro.core.ui.theme.LastroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastroTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Voltar",
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

@Preview(name = "TopBar com voltar - Dark")
@Composable
private fun PreviewWithBack() = LastroTheme {
    LastroTopBar(title = "Transações", onNavigateBack = {})
}

@Preview(name = "TopBar sem voltar - Dark")
@Composable
private fun PreviewWithoutBack() = LastroTheme {
    LastroTopBar(title = "Dashboard")
}

@Preview(name = "TopBar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroTopBar(title = "Perfil", onNavigateBack = {})
}
