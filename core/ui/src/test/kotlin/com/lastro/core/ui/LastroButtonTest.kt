package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
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
    fun hides_text_and_shows_loading_indicator_when_loading() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}, loading = true) }
        }
        composeRule.onNodeWithText("Entrar").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Carregando").assertIsDisplayed()
    }

    @Test
    fun is_disabled_when_enabled_false() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Entrar", onClick = {}, enabled = false) }
        }
        composeRule.onNodeWithText("Entrar").assertIsNotEnabled()
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
            LastroTheme { LastroButton(text = "Criar conta", onClick = {}, variant = ButtonVariant.Outlined) }
        }
        composeRule.onNodeWithText("Criar conta").assertIsDisplayed()
    }

    @Test
    fun ghost_variant_renders_text() {
        composeRule.setContent {
            LastroTheme { LastroButton(text = "Pular", onClick = {}, variant = ButtonVariant.Ghost) }
        }
        composeRule.onNodeWithText("Pular").assertIsDisplayed()
    }
}
