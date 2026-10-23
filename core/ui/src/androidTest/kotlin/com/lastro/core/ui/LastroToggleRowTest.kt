package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import com.lastro.core.ui.components.LastroToggleRow
import com.lastro.core.ui.theme.LastroTheme

class LastroToggleRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_title_and_subtitle() {
        composeRule.setContent {
            LastroTheme {
                LastroToggleRow(
                    title = "Compartilhar com casal",
                    subtitle = "Visível ao parceiro",
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }
        composeRule.onNodeWithText("Compartilhar com casal").assertIsDisplayed()
        composeRule.onNodeWithText("Visível ao parceiro").assertIsDisplayed()
    }

    @Test
    fun clicking_row_fires_callback_with_toggled_value() {
        var newValue: Boolean? = null
        composeRule.setContent {
            LastroTheme {
                LastroToggleRow(
                    title = "Recorrente",
                    checked = false,
                    onCheckedChange = { newValue = it },
                )
            }
        }
        composeRule.onNodeWithText("Recorrente").performClick()
        assertTrue(newValue == true)
    }
}
