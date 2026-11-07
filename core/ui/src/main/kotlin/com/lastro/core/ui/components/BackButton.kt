package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(LastroIconWrapSize.sm)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = "Voltar"
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = LastroIcons.ArrowLeft),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(LastroIconSize.sm),
        )
    }
}

@Preview(name = "BackButton - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { LastroBackButton(onClick = {}) }

@Preview(name = "BackButton - Dark")
@Composable
private fun PreviewDark() = LastroTheme { LastroBackButton(onClick = {}) }
