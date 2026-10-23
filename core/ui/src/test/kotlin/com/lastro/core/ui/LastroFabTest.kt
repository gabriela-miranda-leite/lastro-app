package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.lastro.core.ui.components.LastroFab
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroFabTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_with_content_description() {
        composeRule.setContent {
            LastroTheme { LastroFab(onClick = {}, contentDescription = "Nova transação") }
        }
        composeRule.onNodeWithContentDescription("Nova transação").assertIsDisplayed()
    }

    @Test
    fun click_fires_callback() {
        var clicked = false
        composeRule.setContent {
            LastroTheme { LastroFab(onClick = { clicked = true }, contentDescription = "Nova transação") }
        }
        composeRule.onNodeWithContentDescription("Nova transação").performClick()
        assertTrue(clicked)
    }
}
