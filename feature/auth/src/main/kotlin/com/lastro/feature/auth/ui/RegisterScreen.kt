package com.lastro.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.LastroBackButton
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.components.LastroTextField
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
    val hasNumber = password.any { it.isDigit() || !it.isLetterOrDigit() }
    val passwordsMatch = password.isNotEmpty() && password == confirmPassword

    val canSubmit = name.isNotBlank() && email.isNotBlank() &&
        hasMinLength && hasUppercase && hasNumber &&
        passwordsMatch && termsAccepted

    Scaffold(
        bottomBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = LastroSpacing.screenHorizontal)
                        .padding(top = LastroSpacing.md, bottom = LastroSpacing.bottomSafe),
                ) {
                    LastroButton(
                        text = "Continuar",
                        onClick = onRegister,
                        loading = uiState.isLoading,
                        enabled = canSubmit,
                    )
                    Spacer(modifier = Modifier.height(LastroSpacing.md))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onBack),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Já tem conta? ")
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) { append("Entrar") }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LastroSpacing.md),
            ) {
                LastroBackButton(onClick = onBack)
                Text(
                    text = "Criar conta",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            LastroStepIndicator(
                totalSteps = 3,
                currentStep = 1,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Text(
                text = "Seus dados",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Crie sua conta para começar a controlar suas finanças.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.error != null) LastroBanner(message = uiState.error)
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
                type = TextFieldType.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
            Text(
                text = "Imutável após o cadastro.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = LastroSpacing.xs, start = 2.dp),
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

            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            PasswordRequirement(met = hasMinLength, text = "Mínimo 8 caracteres")
            PasswordRequirement(met = hasUppercase, text = "Letra maiúscula")
            PasswordRequirement(met = hasNumber, text = "Número ou símbolo")

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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTermsChange(!termsAccepted) },
                horizontalArrangement = Arrangement.spacedBy(LastroSpacing.md),
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (termsAccepted) LastroColors.Accent else Color.Transparent)
                        .border(
                            1.dp,
                            if (termsAccepted) LastroColors.Accent else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(6.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (termsAccepted) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
                Text(
                    text = buildAnnotatedString {
                        append("Li e aceito os ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                            append("Termos de Uso")
                        }
                        append(" e a ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                            append("Política de Privacidade")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))
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
private fun RegisterScreenPreview() = LastroTheme(darkTheme = false) {
    RegisterScreenContent(
        uiState = AuthUiState(),
        name = "", email = "", password = "", confirmPassword = "", termsAccepted = false,
        onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
        onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
    )
}

@Preview(name = "Register - Preenchido")
@Composable
private fun RegisterScreenFilledPreview() = LastroTheme(darkTheme = false) {
    RegisterScreenContent(
        uiState = AuthUiState(),
        name = "Gabriela", email = "gabriela@lastro.app", password = "Senha123!", confirmPassword = "Senha123!", termsAccepted = true,
        onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
        onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
    )
}

@Preview(name = "Register - Erro")
@Composable
private fun RegisterScreenErrorPreview() = LastroTheme(darkTheme = false) {
    RegisterScreenContent(
        uiState = AuthUiState(error = "Email já cadastrado"),
        name = "Gabriela", email = "gabriela@lastro.app", password = "Senha123!", confirmPassword = "Senha123!", termsAccepted = true,
        onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
        onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
    )
}
