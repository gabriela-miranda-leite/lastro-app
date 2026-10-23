package com.lastro.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lastro.core.ui.components.BannerType
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LastroBannerTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_error_message() {
        composeRule.setContent {
            LastroTheme { LastroBanner(message = "Email ou senha incorretos.") }
        }
        composeRule.onNodeWithText("Email ou senha incorretos.").assertIsDisplayed()
    }

    @Test
    fun renders_success_message() {
        composeRule.setContent {
            LastroTheme { LastroBanner(message = "Convite enviado!", type = BannerType.Success) }
        }
        composeRule.onNodeWithText("Convite enviado!").assertIsDisplayed()
    }

    @Test
    fun renders_info_message() {
        composeRule.setContent {
            LastroTheme { LastroBanner(message = "Regra 50/30/20", type = BannerType.Info) }
        }
        composeRule.onNodeWithText("Regra 50/30/20").assertIsDisplayed()
    }
}
