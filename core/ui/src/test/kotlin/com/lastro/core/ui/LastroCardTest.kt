package com.lastro.core.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.LastroCard
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_content() {
        composeRule.setContent {
            LastroTheme { LastroCard { Text("Saldo total") } }
        }
        composeRule.onNodeWithText("Saldo total").assertIsDisplayed()
    }

    @Test
    fun renders_multiple_children() {
        composeRule.setContent {
            LastroTheme {
                LastroCard {
                    Text("Receita")
                    Text("R$ 5.000,00")
                }
            }
        }
        composeRule.onNodeWithText("Receita").assertIsDisplayed()
        composeRule.onNodeWithText("R$ 5.000,00").assertIsDisplayed()
    }
}
