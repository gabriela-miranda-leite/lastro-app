package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LastroSpacing.xl, vertical = LastroSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onNavigateBack != null) {
            LastroBackButton(onClick = onNavigateBack)
            Spacer(modifier = Modifier.width(LastroSpacing.md))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        actions()
    }
}

@Composable
fun LastroTopBarAction(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(name = "TopBar com voltar - Dark")
@Composable
private fun PreviewWithBack() = LastroTheme {
    LastroTopBar(title = "Transações", onNavigateBack = {}) {
        LastroTopBarAction(onClick = {}, contentDescription = "Filtrar") {
            Icon(
                painter = painterResource(LastroIcons.Tag),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(LastroIconSize.xs),
            )
        }
    }
}

@Preview(name = "TopBar sem voltar - Dark")
@Composable
private fun PreviewWithoutBack() = LastroTheme {
    LastroTopBar(title = "Início")
}

@Preview(name = "TopBar - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroTopBar(title = "Perfil", onNavigateBack = {})
}
