package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroTextFieldTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_label() {
        composeRule.setContent {
            LastroTheme { LastroTextField(value = "", onValueChange = {}, label = "Email") }
        }
        composeRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun displays_error_message_when_error_is_not_null() {
        composeRule.setContent {
            LastroTheme {
                LastroTextField(
                    value = "",
                    onValueChange = {},
                    label = "Email",
                    error = "Campo obrigatório",
                )
            }
        }
        composeRule.onNodeWithText("Campo obrigatório").assertIsDisplayed()
    }

    @Test
    fun money_type_renders_label() {
        composeRule.setContent {
            LastroTheme {
                LastroTextField(
                    value = "25000",
                    onValueChange = {},
                    label = "Valor",
                    type = TextFieldType.Money,
                )
            }
        }
        composeRule.onNodeWithText("VALOR").assertIsDisplayed()
    }

    @Test
    fun password_type_renders_label() {
        composeRule.setContent {
            LastroTheme {
                LastroTextField(
                    value = "senha123",
                    onValueChange = {},
                    label = "Senha",
                    type = TextFieldType.Password,
                )
            }
        }
        composeRule.onNodeWithText("SENHA").assertIsDisplayed()
    }

    @Test
    fun password_type_shows_visibility_toggle_icon() {
        composeRule.setContent {
            LastroTheme {
                LastroTextField(
                    value = "senha123",
                    onValueChange = {},
                    label = "Senha",
                    type = TextFieldType.Password,
                )
            }
        }
        composeRule.onNodeWithContentDescriptionSubstring("senha").assertIsDisplayed()
    }

    private fun androidx.compose.ui.test.SemanticsNodeInteractionsProvider.onNodeWithContentDescriptionSubstring(
        substring: String,
    ) = onNode(
        androidx.compose.ui.test.hasContentDescriptionExactly("Mostrar senha")
            .or(androidx.compose.ui.test.hasContentDescriptionExactly("Ocultar senha"))
    )
}
