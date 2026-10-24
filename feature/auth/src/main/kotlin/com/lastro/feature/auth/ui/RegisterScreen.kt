package com.lastro.feature.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.ErrorBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.LastroToggleRow
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.AuthEvent
import com.lastro.feature.auth.viewmodel.AuthUiState
import com.lastro.feature.auth.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var termsAccepted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.RegisterSuccess -> onRegisterSuccess()
                else -> Unit
            }
        }
    }

    RegisterScreenContent(
        uiState = uiState,
        name = name,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        termsAccepted = termsAccepted,
        onNameChange = { name = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onTermsChange = { termsAccepted = it },
        onRegister = { viewModel.register(email, password, name) },
        onBack = onBack,
    )
}

@Composable
internal fun RegisterScreenContent(
    uiState: AuthUiState,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    termsAccepted: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit,
) {
    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasNumber = password.any { it.isDigit() }
    val passwordsMatch = password.isNotEmpty() && password == confirmPassword

    val canSubmit = name.isNotBlank() && email.isNotBlank() &&
        hasMinLength && hasUppercase && hasNumber &&
        passwordsMatch && termsAccepted

    Scaffold(
        topBar = { LastroTopBar(title = "Criar conta", onNavigateBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroStepIndicator(
                totalSteps = 3,
                currentStep = 0,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            ErrorBanner(
                message = uiState.error ?: "",
                visible = uiState.error != null,
            )

            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Nome completo",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LastroSpacing.sm))
                PasswordRequirement(met = hasMinLength, text = "Mínimo 8 caracteres")
                PasswordRequirement(met = hasUppercase, text = "Uma letra maiúscula")
                PasswordRequirement(met = hasNumber, text = "Um número")
            }

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirmar senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            LastroToggleRow(
                title = "Aceito os termos e condições",
                checked = termsAccepted,
                onCheckedChange = onTermsChange,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            LastroButton(
                text = "Continuar",
                onClick = onRegister,
                loading = uiState.isLoading,
                enabled = canSubmit,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroButton(
                text = "Já tenho conta",
                onClick = onBack,
                variant = ButtonVariant.Ghost,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.bottomSafe))
        }
    }
}

@Composable
private fun PasswordRequirement(met: Boolean, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp),
    ) {
        if (met) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = LastroColors.Positive,
                modifier = Modifier.size(14.dp),
            )
        } else {
            Text(
                text = "·",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = "  $text",
            style = MaterialTheme.typography.labelSmall,
            color = if (met) LastroColors.Positive else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(name = "Register - Default")
@Composable
private fun RegisterScreenPreview() = LastroTheme {
    RegisterScreenContent(
        uiState = AuthUiState(),
        name = "",
        email = "",
        password = "",
        confirmPassword = "",
        termsAccepted = false,
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onTermsChange = {},
        onRegister = {},
        onBack = {},
    )
}

@Preview(name = "Register - Preenchido")
@Composable
private fun RegisterScreenFilledPreview() = LastroTheme {
    RegisterScreenContent(
        uiState = AuthUiState(),
        name = "Gabriela",
        email = "gabriela@lastro.app",
        password = "Senha123",
        confirmPassword = "Senha123",
        termsAccepted = true,
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onTermsChange = {},
        onRegister = {},
        onBack = {},
    )
}

@Preview(name = "Register - Erro email duplicado")
@Composable
private fun RegisterScreenErrorPreview() = LastroTheme {
    RegisterScreenContent(
        uiState = AuthUiState(error = "email already in use"),
        name = "Gabriela",
        email = "gabriela@lastro.app",
        password = "Senha123",
        confirmPassword = "Senha123",
        termsAccepted = true,
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onTermsChange = {},
        onRegister = {},
        onBack = {},
    )
}
