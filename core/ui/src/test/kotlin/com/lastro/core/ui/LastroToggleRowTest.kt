package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.LastroToggleRow
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroToggleRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_title() {
        composeRule.setContent {
            LastroTheme { LastroToggleRow(title = "Compartilhar", checked = false, onCheckedChange = {}) }
        }
        composeRule.onNodeWithText("Compartilhar").assertIsDisplayed()
    }

    @Test
    fun renders_subtitle_when_provided() {
        composeRule.setContent {
            LastroTheme {
                LastroToggleRow(
                    title = "Compartilhar",
                    subtitle = "Visível ao parceiro",
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }
        composeRule.onNodeWithText("Visível ao parceiro").assertIsDisplayed()
    }

    @Test
    fun clicking_row_calls_callback_with_toggled_value() {
        var newValue: Boolean? = null
        composeRule.setContent {
            LastroTheme {
                LastroToggleRow(title = "Recorrente", checked = false, onCheckedChange = { newValue = it })
            }
        }
        composeRule.onNodeWithText("Recorrente").performClick()
        assertTrue(newValue == true)
    }

    @Test
    fun clicking_checked_row_calls_callback_with_false() {
        var newValue: Boolean? = null
        composeRule.setContent {
            LastroTheme {
                LastroToggleRow(title = "Recorrente", checked = true, onCheckedChange = { newValue = it })
            }
        }
        composeRule.onNodeWithText("Recorrente").performClick()
        assertTrue(newValue == false)
    }
}
