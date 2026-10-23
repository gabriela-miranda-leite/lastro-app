package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroTopBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_title() {
        composeRule.setContent {
            LastroTheme { LastroTopBar(title = "Transações") }
        }
        composeRule.onNodeWithText("Transações").assertIsDisplayed()
    }

    @Test
    fun back_button_is_not_shown_without_callback() {
        composeRule.setContent {
            LastroTheme { LastroTopBar(title = "Dashboard") }
        }
        composeRule.onNodeWithContentDescription("Voltar").assertDoesNotExist()
    }

    @Test
    fun back_button_is_shown_when_callback_provided() {
        composeRule.setContent {
            LastroTheme { LastroTopBar(title = "Perfil", onNavigateBack = {}) }
        }
        composeRule.onNodeWithContentDescription("Voltar").assertIsDisplayed()
    }

    @Test
    fun clicking_back_fires_callback() {
        var clicked = false
        composeRule.setContent {
            LastroTheme { LastroTopBar(title = "Perfil", onNavigateBack = { clicked = true }) }
        }
        composeRule.onNodeWithContentDescription("Voltar").performClick()
        assertTrue(clicked)
    }
}
