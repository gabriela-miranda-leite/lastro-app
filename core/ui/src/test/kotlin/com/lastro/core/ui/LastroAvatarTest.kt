package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.LastroAvatar
import com.lastro.core.ui.components.LastroAvatarOverlap
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroAvatarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_initial_uppercased() {
        composeRule.setContent {
            LastroTheme { LastroAvatar(initial = "g") }
        }
        composeRule.onNodeWithText("G").assertIsDisplayed()
    }

    @Test
    fun renders_only_first_char_when_multiple_given() {
        composeRule.setContent {
            LastroTheme { LastroAvatar(initial = "GA") }
        }
        composeRule.onNodeWithText("G").assertIsDisplayed()
    }

    @Test
    fun overlap_renders_both_initials() {
        composeRule.setContent {
            LastroTheme { LastroAvatarOverlap(initials = listOf("G", "M")) }
        }
        composeRule.onNodeWithText("G").assertIsDisplayed()
        composeRule.onNodeWithText("M").assertIsDisplayed()
    }
}
