package com.lastro.feature.auth.screenshot

import androidx.compose.ui.test.junit4.createComposeRule
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
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel6)
class AuthScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_default() {
        composeTestRule.captureRoboImage {
            LastroTheme {
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
    }

    @Test
    fun loginScreen_filled() {
        composeTestRule.captureRoboImage {
            LastroTheme {
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
    }

    @Test
    fun loginScreen_error_401() {
        composeTestRule.captureRoboImage {
            LastroTheme {
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
    }

    @Test
    fun loginScreen_loading() {
        composeTestRule.captureRoboImage {
            LastroTheme {
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
    }

    @Test
    fun registerScreen_default() {
        composeTestRule.captureRoboImage {
            LastroTheme {
                RegisterScreenContent(
                    uiState = AuthUiState(),
                    name = "",
                    email = "",
                    password = "",
                    onNameChange = {},
                    onEmailChange = {},
                    onPasswordChange = {},
                    onRegister = {},
                    onBack = {},
                )
            }
        }
    }

    @Test
    fun registerScreen_error() {
        composeTestRule.captureRoboImage {
            LastroTheme {
                RegisterScreenContent(
                    uiState = AuthUiState(error = "email already in use"),
                    name = "Gabriela",
                    email = "gabriela@lastro.app",
                    password = "senha123",
                    onNameChange = {},
                    onEmailChange = {},
                    onPasswordChange = {},
                    onRegister = {},
                    onBack = {},
                )
            }
        }
    }

    @Test
    fun forgotPasswordScreen_default() {
        composeTestRule.captureRoboImage {
            LastroTheme {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(),
                    email = "",
                    onEmailChange = {},
                    onSubmit = {},
                    onBack = {},
                )
            }
        }
    }

    @Test
    fun forgotPasswordScreen_success() {
        composeTestRule.captureRoboImage {
            LastroTheme {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(success = true),
                    email = "gabriela@lastro.app",
                    onEmailChange = {},
                    onSubmit = {},
                    onBack = {},
                )
            }
        }
    }

    @Test
    fun forgotPasswordScreen_error() {
        composeTestRule.captureRoboImage {
            LastroTheme {
                ForgotPasswordScreenContent(
                    uiState = ForgotPasswordUiState(error = "Muitas tentativas. Aguarde alguns minutos"),
                    email = "gabriela@lastro.app",
                    onEmailChange = {},
                    onSubmit = {},
                    onBack = {},
                )
            }
        }
    }
}
