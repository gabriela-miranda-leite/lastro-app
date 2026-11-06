package com.lastro.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroTheme

data class BottomNavItem(
    val label: String,
    @DrawableRes val iconRes: Int,
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
                        painter = painterResource(item.iconRes),
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
    BottomNavItem("Dashboard", LastroIcons.Dashboard, "Dashboard"),
    BottomNavItem("Transações", LastroIcons.List, "Transações"),
    BottomNavItem("Perfil", LastroIcons.UserCircle, "Perfil"),
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
