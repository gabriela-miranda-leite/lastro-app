package com.lastro.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.BottomNavItem
import com.lastro.core.ui.components.LastroBottomNavigation
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroBottomNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val items = listOf(
        BottomNavItem("Dashboard", Icons.Outlined.Dashboard, "Dashboard"),
        BottomNavItem("Perfil", Icons.Outlined.Person, "Perfil"),
    )

    @Test
    fun renders_all_item_labels() {
        composeRule.setContent {
            LastroTheme { LastroBottomNavigation(items = items, selectedIndex = 0, onItemSelected = {}) }
        }
        composeRule.onNodeWithText("Dashboard").assertIsDisplayed()
        composeRule.onNodeWithText("Perfil").assertIsDisplayed()
    }

    @Test
    fun clicking_second_item_fires_callback_with_index_1() {
        var selected = 0
        composeRule.setContent {
            LastroTheme {
                LastroBottomNavigation(items = items, selectedIndex = 0, onItemSelected = { selected = it })
            }
        }
        composeRule.onNodeWithText("Perfil").performClick()
        assertEquals(1, selected)
    }

    @Test
    fun clicking_first_item_fires_callback_with_index_0() {
        var selected = -1
        composeRule.setContent {
            LastroTheme {
                LastroBottomNavigation(items = items, selectedIndex = 1, onItemSelected = { selected = it })
            }
        }
        composeRule.onNodeWithText("Dashboard").performClick()
        assertEquals(0, selected)
    }
}
