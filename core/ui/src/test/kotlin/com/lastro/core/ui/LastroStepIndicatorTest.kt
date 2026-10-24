package com.lastro.core.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.assertIsDisplayed
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroStepIndicatorTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_without_crash_for_three_steps() {
        composeRule.setContent {
            LastroTheme { LastroStepIndicator(totalSteps = 3, currentStep = 0) }
        }
        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun renders_with_current_step_at_end() {
        composeRule.setContent {
            LastroTheme { LastroStepIndicator(totalSteps = 3, currentStep = 2) }
        }
        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun renders_single_step() {
        composeRule.setContent {
            LastroTheme { LastroStepIndicator(totalSteps = 1, currentStep = 0) }
        }
        composeRule.onRoot().assertIsDisplayed()
    }
}
