package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.lastro.core.ui.components.LastroSegmentedControl
import com.lastro.core.ui.theme.LastroTheme

class LastroSegmentedControlTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val options = listOf("Você", "Casal")

    @Test
    fun renders_all_options() {
        composeRule.setContent {
            LastroTheme {
                LastroSegmentedControl(options = options, selectedIndex = 0, onOptionSelected = {})
            }
        }
        options.forEach { composeRule.onNodeWithText(it).assertIsDisplayed() }
    }

    @Test
    fun clicking_second_option_fires_callback_with_index_1() {
        var selected = 0
        composeRule.setContent {
            LastroTheme {
                LastroSegmentedControl(options = options, selectedIndex = 0, onOptionSelected = { selected = it })
            }
        }
        composeRule.onNodeWithText("Casal").performClick()
        assertEquals(1, selected)
    }

    @Test
    fun clicking_first_option_fires_callback_with_index_0() {
        var selected = -1
        composeRule.setContent {
            LastroTheme {
                LastroSegmentedControl(options = options, selectedIndex = 1, onOptionSelected = { selected = it })
            }
        }
        composeRule.onNodeWithText("Você").performClick()
        assertEquals(0, selected)
    }
}
