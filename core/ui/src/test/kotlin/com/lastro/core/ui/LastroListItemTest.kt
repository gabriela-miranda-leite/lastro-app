package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.LastroListItem
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroListItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_title() {
        composeRule.setContent {
            LastroTheme { LastroListItem(title = "Nome") }
        }
        composeRule.onNodeWithText("Nome").assertIsDisplayed()
    }

    @Test
    fun renders_subtitle_when_provided() {
        composeRule.setContent {
            LastroTheme { LastroListItem(title = "E-mail", subtitle = "gabriela@email.com") }
        }
        composeRule.onNodeWithText("gabriela@email.com").assertIsDisplayed()
    }

    @Test
    fun click_fires_callback() {
        var clicked = false
        composeRule.setContent {
            LastroTheme { LastroListItem(title = "Alterar senha", onClick = { clicked = true }) }
        }
        composeRule.onNodeWithText("Alterar senha").performClick()
        assertTrue(clicked)
    }

    @Test
    fun no_click_callback_does_not_crash() {
        composeRule.setContent {
            LastroTheme { LastroListItem(title = "Versão do app", subtitle = "1.0.0") }
        }
        composeRule.onNodeWithText("Versão do app").assertIsDisplayed()
    }
}
