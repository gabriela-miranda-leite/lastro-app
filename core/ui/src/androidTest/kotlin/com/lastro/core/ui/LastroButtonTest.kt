package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.theme.LastroTheme

class LastroButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_text_when_not_loading() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}) }
        }
        composeRule.onNodeWithText("Entrar").assertIsDisplayed()
    }

    @Test
    fun shows_loading_indicator_and_hides_text_when_loading() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}, loading = true) }
        }
        composeRule.onNodeWithText("Entrar").assertDoesNotExist()
    }

    @Test
    fun button_is_disabled_when_enabled_false() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}, enabled = false) }
        }
        composeRule.onNodeWithText("Entrar").assertIsNotEnabled()
    }

    @Test
    fun button_is_disabled_when_loading() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}, loading = true) }
        }
        composeRule.onNodeWithContentDescription("Carregando").assertIsDisplayed()
    }

    @Test
    fun click_fires_callback() {
        var clicked = false
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = { clicked = true }) }
        }
        composeRule.onNodeWithText("Entrar").performClick()
        assertTrue(clicked)
    }

    @Test
    fun outlined_variant_renders_text() {
        composeRule.setContent {
            LastroTheme {
                LastroButton(text = "Criar conta", onClick = {}, variant = ButtonVariant.Outlined)
            }
        }
        composeRule.onNodeWithText("Criar conta").assertIsDisplayed()
    }

    @Test
    fun ghost_variant_renders_text() {
        composeRule.setContent {
            LastroTheme {
                LastroButton(text = "Pular", onClick = {}, variant = ButtonVariant.Ghost)
            }
        }
        composeRule.onNodeWithText("Pular").assertIsDisplayed()
    }
}
