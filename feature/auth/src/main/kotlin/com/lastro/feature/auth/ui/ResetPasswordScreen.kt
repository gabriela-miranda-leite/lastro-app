package com.lastro.feature.auth.ui

import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.UiText
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.resolve
import com.lastro.feature.auth.R
import com.lastro.core.ui.components.LastroBackButton
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.ResetPasswordEvent
import com.lastro.feature.auth.viewmodel.ResetPasswordUiState
import com.lastro.feature.auth.viewmodel.ResetPasswordViewModel

@Composable
fun ResetPasswordScreen(
    token: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ResetPasswordEvent.Success -> onSuccess()
            }
        }
    }

    ResetPasswordScreenContent(
        uiState = uiState,
        newPassword = newPassword,
        confirmPassword = confirmPassword,
        onNewPasswordChange = { newPassword = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onSubmit = { viewModel.submit(token, newPassword) },
        onBack = onBack,
    )
}

@Composable
internal fun ResetPasswordScreenContent(
    uiState: ResetPasswordUiState,
    newPassword: String,
    confirmPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasNumber = newPassword.any { it.isDigit() || !it.isLetterOrDigit() }
    val passwordsMatch = newPassword.isNotEmpty() && newPassword == confirmPassword
    val canSubmit = hasMinLength && hasUppercase && hasNumber && passwordsMatch

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroBackButton(onClick = onBack)

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Text(
                text = "Nova senha",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Escolha uma senha forte para proteger sua conta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.error != null) LastroBanner(message = uiState.error!!.resolve())
            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = "Nova senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            PasswordRequirement(met = hasMinLength, text = "Mínimo 8 caracteres")
            PasswordRequirement(met = hasUppercase, text = "Letra maiúscula")
            PasswordRequirement(met = hasNumber, text = "Número ou símbolo")

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirmar nova senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            LastroButton(
                text = "Redefinir senha",
                onClick = onSubmit,
                loading = uiState.isLoading,
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))
        }
    }
}

@Preview(name = "ResetPassword - Default")
@Composable
private fun ResetPasswordPreview() = LastroTheme(darkTheme = false) {
    ResetPasswordScreenContent(
        uiState = ResetPasswordUiState(),
        newPassword = "",
        confirmPassword = "",
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onSubmit = {},
        onBack = {},
    )
}

@Preview(name = "ResetPassword - Erro")
@Composable
private fun ResetPasswordErrorPreview() = LastroTheme(darkTheme = false) {
    ResetPasswordScreenContent(
        uiState = ResetPasswordUiState(error = UiText.Res(R.string.reset_password_error_link)),
        newPassword = "Senha123!",
        confirmPassword = "Senha123!",
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onSubmit = {},
        onBack = {},
    )
}
