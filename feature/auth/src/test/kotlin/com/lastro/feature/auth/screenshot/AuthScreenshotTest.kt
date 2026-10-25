package com.lastro.feature.auth.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.ui.ForgotPasswordScreenContent
import com.lastro.feature.auth.ui.LoginScreenContent
import com.lastro.feature.auth.ui.OnboardingIncomeScreenContent
import com.lastro.feature.auth.ui.RegisterScreenContent
import com.lastro.feature.auth.ui.ResetPasswordScreenContent
import com.lastro.feature.auth.viewmodel.AuthUiState
import com.lastro.feature.auth.viewmodel.ForgotPasswordUiState
import com.lastro.feature.auth.viewmodel.OnboardingIncomeUiState
import com.lastro.feature.auth.viewmodel.ResetPasswordUiState
import org.junit.Rule
import org.junit.Test

class LoginScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    @Test fun default() = paparazzi.snapshot { LastroTheme(darkTheme = false) { LoginScreenContent(uiState = AuthUiState(), email = "", password = "", onEmailChange = {}, onPasswordChange = {}, onLogin = {}, onForgotPassword = {}, onRegister = {}) } }

    @Test fun filled() = paparazzi.snapshot { LastroTheme(darkTheme = false) { LoginScreenContent(uiState = AuthUiState(), email = "gabriela@lastro.app", password = "senha123", onEmailChange = {}, onPasswordChange = {}, onLogin = {}, onForgotPassword = {}, onRegister = {}) } }

    @Test fun loading() = paparazzi.snapshot { LastroTheme(darkTheme = false) { LoginScreenContent(uiState = AuthUiState(isLoading = true), email = "gabriela@lastro.app", password = "senha123", onEmailChange = {}, onPasswordChange = {}, onLogin = {}, onForgotPassword = {}, onRegister = {}) } }

    @Test fun error_401() = paparazzi.snapshot { LastroTheme(darkTheme = false) { LoginScreenContent(uiState = AuthUiState(error = ApiException.Unauthorized.message), email = "gabriela@lastro.app", password = "errada", onEmailChange = {}, onPasswordChange = {}, onLogin = {}, onForgotPassword = {}, onRegister = {}) } }

    @Test fun default_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { LoginScreenContent(uiState = AuthUiState(), email = "", password = "", onEmailChange = {}, onPasswordChange = {}, onLogin = {}, onForgotPassword = {}, onRegister = {}) } }
}

class RegisterScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    @Test fun default() = paparazzi.snapshot { LastroTheme(darkTheme = false) { RegisterScreenContent(uiState = AuthUiState(), name = "", email = "", password = "", confirmPassword = "", termsAccepted = false, onNameChange = {}, onEmailChange = {}, onPasswordChange = {}, onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {}) } }

    @Test fun error() = paparazzi.snapshot { LastroTheme(darkTheme = false) { RegisterScreenContent(uiState = AuthUiState(error = "Email já cadastrado"), name = "Gabriela", email = "gabriela@lastro.app", password = "Senha123!", confirmPassword = "Senha123!", termsAccepted = true, onNameChange = {}, onEmailChange = {}, onPasswordChange = {}, onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {}) } }

    @Test fun default_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { RegisterScreenContent(uiState = AuthUiState(), name = "", email = "", password = "", confirmPassword = "", termsAccepted = false, onNameChange = {}, onEmailChange = {}, onPasswordChange = {}, onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {}) } }
}

class ForgotPasswordScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    @Test fun default() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(), email = "", onEmailChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun success() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(success = true), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun error() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(error = "Muitas tentativas. Aguarde alguns minutos"), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun default_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(), email = "", onEmailChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun success_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(success = true), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {}) } }
}

class OnboardingIncomeScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    @Test fun default() = paparazzi.snapshot { LastroTheme(darkTheme = false) { OnboardingIncomeScreenContent(uiState = OnboardingIncomeUiState(), income = "", onIncomeChange = {}, onSubmit = {}) } }

    @Test fun filled() = paparazzi.snapshot { LastroTheme(darkTheme = false) { OnboardingIncomeScreenContent(uiState = OnboardingIncomeUiState(), income = "5000", onIncomeChange = {}, onSubmit = {}) } }

    @Test fun error() = paparazzi.snapshot { LastroTheme(darkTheme = false) { OnboardingIncomeScreenContent(uiState = OnboardingIncomeUiState(error = "Não foi possível salvar a renda. Tente novamente"), income = "5000", onIncomeChange = {}, onSubmit = {}) } }

    @Test fun default_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { OnboardingIncomeScreenContent(uiState = OnboardingIncomeUiState(), income = "", onIncomeChange = {}, onSubmit = {}) } }
}

class ResetPasswordScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    @Test fun default() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ResetPasswordScreenContent(uiState = ResetPasswordUiState(), newPassword = "", confirmPassword = "", onNewPasswordChange = {}, onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun filled() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ResetPasswordScreenContent(uiState = ResetPasswordUiState(), newPassword = "Senha123!", confirmPassword = "Senha123!", onNewPasswordChange = {}, onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun error() = paparazzi.snapshot { LastroTheme(darkTheme = false) { ResetPasswordScreenContent(uiState = ResetPasswordUiState(error = "Link inválido ou expirado. Solicite um novo link."), newPassword = "Senha123!", confirmPassword = "Senha123!", onNewPasswordChange = {}, onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}) } }

    @Test fun default_dark() = paparazzi.snapshot { LastroTheme(darkTheme = true) { ResetPasswordScreenContent(uiState = ResetPasswordUiState(), newPassword = "", confirmPassword = "", onNewPasswordChange = {}, onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}) } }
}
