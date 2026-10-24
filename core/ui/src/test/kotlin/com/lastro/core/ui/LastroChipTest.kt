package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.LastroChip
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroChipTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_label() {
        composeRule.setContent {
            LastroTheme { LastroChip(label = "Necessidade", selected = false, onClick = {}) }
        }
        composeRule.onNodeWithText("Necessidade").assertIsDisplayed()
    }

    @Test
    fun click_fires_callback() {
        var clicked = false
        composeRule.setContent {
            LastroTheme { LastroChip(label = "Lazer", selected = false, onClick = { clicked = true }) }
        }
        composeRule.onNodeWithText("Lazer").performClick()
        assertTrue(clicked)
    }

    @Test
    fun selected_chip_still_renders_label() {
        composeRule.setContent {
            LastroTheme { LastroChip(label = "Meta", selected = true, onClick = {}) }
        }
        composeRule.onNodeWithText("Meta").assertIsDisplayed()
    }
}
