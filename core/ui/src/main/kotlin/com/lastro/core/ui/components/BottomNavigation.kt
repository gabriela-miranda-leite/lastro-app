package com.lastro.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import com.lastro.core.ui.theme.LastroTheme

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
)

@Composable
fun LastroBottomNavigation(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = androidx.compose.ui.unit.Dp.Unspecified,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription,
                    )
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

private val previewItems = listOf(
    BottomNavItem("Dashboard", Icons.Outlined.Dashboard, "Dashboard"),
    BottomNavItem("Transações", Icons.Outlined.SwapHoriz, "Transações"),
    BottomNavItem("Fixos", Icons.Outlined.Timer, "Fixos"),
    BottomNavItem("Perfil", Icons.Outlined.Person, "Perfil"),
)

@Preview(name = "BottomNav - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroBottomNavigation(items = previewItems, selectedIndex = 0, onItemSelected = {})
}

@Preview(name = "BottomNav - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroBottomNavigation(items = previewItems, selectedIndex = 1, onItemSelected = {})
}
