package com.lastro.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.LastroBottomSheet
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_title_when_provided() {
        composeRule.setContent {
            LastroTheme {
                LastroBottomSheet(onDismiss = {}, title = "Nova Transação") {
                    Text("Conteúdo")
                }
            }
        }
        composeRule.onNodeWithText("Nova Transação").assertIsDisplayed()
    }

    @Test
    fun renders_content() {
        composeRule.setContent {
            LastroTheme {
                LastroBottomSheet(onDismiss = {}) {
                    Text("Filtrar por categoria")
                }
            }
        }
        composeRule.onNodeWithText("Filtrar por categoria").assertIsDisplayed()
    }

    @Test
    fun renders_without_title() {
        composeRule.setContent {
            LastroTheme {
                LastroBottomSheet(onDismiss = {}) {
                    Text("Sem título")
                }
            }
        }
        composeRule.onNodeWithText("Sem título").assertIsDisplayed()
    }
}
