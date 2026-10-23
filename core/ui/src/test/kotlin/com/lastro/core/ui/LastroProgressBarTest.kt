package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.LastroProgressBar
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroProgressBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_label_when_provided() {
        composeRule.setContent {
            LastroTheme { LastroProgressBar(progress = 0.5f, label = "Necessidades 50%") }
        }
        composeRule.onNodeWithText("Necessidades 50%").assertIsDisplayed()
    }

    @Test
    fun renders_value_when_provided() {
        composeRule.setContent {
            LastroTheme { LastroProgressBar(progress = 0.7f, value = "R$ 3.500 / R$ 5.000") }
        }
        composeRule.onNodeWithText("R$ 3.500 / R$ 5.000").assertIsDisplayed()
    }

    @Test
    fun renders_without_label_or_value() {
        composeRule.setContent {
            LastroTheme { LastroProgressBar(progress = 0.3f) }
        }
    }

    @Test
    fun clamps_progress_above_one() {
        composeRule.setContent {
            LastroTheme { LastroProgressBar(progress = 1.5f, label = "Acima do limite") }
        }
        composeRule.onNodeWithText("Acima do limite").assertIsDisplayed()
    }
}
