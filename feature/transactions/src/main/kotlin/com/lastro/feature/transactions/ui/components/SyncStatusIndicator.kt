package com.lastro.feature.transactions.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.ui.theme.LastroColors

@Composable
fun SyncStatusIndicator(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier,
) {
    val (icon, tint, description) = syncStatus.toIndicatorState()

    if (syncStatus == SyncStatus.PENDING) {
        val transition = rememberInfiniteTransition(label = "sync-rotation")
        val rotation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "rotation",
        )
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = tint,
            modifier = modifier.graphicsLayer { rotationZ = rotation },
        )
    } else {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = tint,
            modifier = modifier,
        )
    }
}

data class SyncIndicatorState(
    val icon: ImageVector,
    val tint: androidx.compose.ui.graphics.Color,
    val contentDescription: String,
)

fun SyncStatus.toIndicatorState(): SyncIndicatorState = when (this) {
    SyncStatus.SYNCED -> SyncIndicatorState(
        icon = Icons.Default.Check,
        tint = LastroColors.Positive,
        contentDescription = "Sincronizado",
    )
    SyncStatus.PENDING -> SyncIndicatorState(
        icon = Icons.Default.Refresh,
        tint = androidx.compose.ui.graphics.Color(0xFFFF9800),
        contentDescription = "Sincronização pendente",
    )
    SyncStatus.ERROR -> SyncIndicatorState(
        icon = Icons.Default.Warning,
        tint = LastroColors.Negative,
        contentDescription = "Erro de sincronização",
    )
}
