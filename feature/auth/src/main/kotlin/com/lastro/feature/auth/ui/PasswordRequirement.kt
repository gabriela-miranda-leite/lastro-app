package com.lastro.feature.auth.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors

@Composable
internal fun PasswordRequirement(met: Boolean, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp),
    ) {
        if (met) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = LastroColors.Positive,
                modifier = Modifier.size(14.dp),
            )
        } else {
            Text(
                text = "·",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = "  $text",
            style = MaterialTheme.typography.labelSmall,
            color = if (met) LastroColors.Positive else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
