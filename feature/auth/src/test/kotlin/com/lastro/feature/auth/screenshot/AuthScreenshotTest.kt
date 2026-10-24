package com.lastro.feature.auth.screenshot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.ui.ForgotPasswordScreenContent
import com.lastro.feature.auth.ui.LoginScreenContent
import com.lastro.feature.auth.ui.RegisterScreenContent
import com.lastro.feature.auth.viewmodel.AuthUiState
import com.lastro.feature.auth.viewmodel.ForgotPasswordUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel6)
class AuthScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_default() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = AuthUiState(),
                    email = "",
                    password = "",
                    onEmailChange = {},
                    onPasswordChange = {},
                    onLogin = {},
                    onForgotPassword = {},
                    onRegister = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(login("default"))
    }

    @Test
    fun loginScreen_filled() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = AuthUiState(),
                    email = "gabriela@lastro.app",
                    password = "senha123",
                    onEmailChange = {},
                    onPasswordChange = {},
                    onLogin = {},
                    onForgotPassword = {},
                    onRegister = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(login("filled"))
    }

    @Test
    fun loginScreen_error_401() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = AuthUiState(error = ApiException.Unauthorized.message),
                    email = "gabriela@lastro.app",
                    password = "errada",
                    onEmailChange = {},
                    onPasswordChange = {},
                    onLogin = {},
                    onForgotPassword = {},
                    onRegister = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(login("error_401"))
    }

    @Test
    fun loginScreen_loading() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = AuthUiState(isLoading = true),
                    email = "gabriela@lastro.app",
                    password = "senha123",
                    onEmailChange = {},
                    onPasswordChange = {},
                    onLogin = {},
                    onForgotPassword = {},
                    onRegister = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(login("loading"))
    }

    @Test
    fun loginScreen_default_dark() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = true) {
                LoginScreenContent(
                    uiState = AuthUiState(), email = "", password = "",
                    onEmailChange = {}, onPasswordChange = {}, onLogin = {},
                    onForgotPassword = {}, onRegister = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(login("default_dark"))
    }

    @Test
    fun registerScreen_default() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                RegisterScreenContent(
                    uiState = AuthUiState(),
                    name = "", email = "", password = "", confirmPassword = "", termsAccepted = false,
                    onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
                    onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(register("default"))
    }

    @Test
    fun registerScreen_error() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                RegisterScreenContent(
                    uiState = AuthUiState(error = "email already in use"),
                    name = "Gabriela",
                    email = "gabriela@lastro.app",
                    password = "Senha123",
                    confirmPassword = "Senha123",
                    termsAccepted = true,
                    onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
                    onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(register("error"))
    }

    @Test
    fun registerScreen_default_dark() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = true) {
                RegisterScreenContent(
                    uiState = AuthUiState(),
                    name = "", email = "", password = "", confirmPassword = "", termsAccepted = false,
                    onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
                    onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(register("default_dark"))
    }

    @Test
    fun forgotPasswordScreen_default() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(), email = "",
                    onEmailChange = {}, onSubmit = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(forgot("default"))
    }

    @Test
    fun forgotPasswordScreen_success() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(success = true),
                    email = "gabriela@lastro.app",
                    onEmailChange = {}, onSubmit = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(forgot("success"))
    }

    @Test
    fun forgotPasswordScreen_error() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = false) {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(error = "Muitas tentativas. Aguarde alguns minutos"),
                    email = "gabriela@lastro.app",
                    onEmailChange = {}, onSubmit = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(forgot("error"))
    }

    @Test
    fun forgotPasswordScreen_default_dark() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = true) {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(), email = "",
                    onEmailChange = {}, onSubmit = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(forgot("default_dark"))
    }

    @Test
    fun forgotPasswordScreen_success_dark() {
        composeTestRule.setContent {
            LastroTheme(darkTheme = true) {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(success = true), email = "gabriela@lastro.app",
                    onEmailChange = {}, onSubmit = {}, onBack = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(forgot("success_dark"))
    }

    private fun login(name: String) = "src/test/snapshots/login/$name.png"
    private fun register(name: String) = "src/test/snapshots/register/$name.png"
    private fun forgot(name: String) = "src/test/snapshots/forgot/$name.png"
}
