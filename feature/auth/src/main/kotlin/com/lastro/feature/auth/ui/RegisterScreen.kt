package com.lastro.feature.auth.ui

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
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
import com.lastro.core.ui.UiText
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.resolve
import com.lastro.feature.auth.R
import com.lastro.core.ui.components.LastroBackButton
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroCard
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
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
    val passwordValid = hasMinLength && hasUppercase && hasNumber
    val passwordsMatch = password.isNotEmpty() && password == confirmPassword

    val isNameValid = name.isNotBlank()
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val canSubmit = isNameValid && isEmailValid &&
        passwordValid && passwordsMatch && termsAccepted

    var nameFocusedOnce by rememberSaveable { mutableStateOf(false) }
    var emailFocusedOnce by rememberSaveable { mutableStateOf(false) }
    var passwordFocusedOnce by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordFocusedOnce by rememberSaveable { mutableStateOf(false) }

    var nameTouched by rememberSaveable { mutableStateOf(false) }
    var emailTouched by rememberSaveable { mutableStateOf(false) }
    var passwordTouched by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordTouched by rememberSaveable { mutableStateOf(false) }

    val nameError = if (nameTouched && !isNameValid) stringResource(R.string.register_error_name_required) else null
    val emailError = if (emailTouched && !isEmailValid) stringResource(R.string.register_error_email_invalid) else null
    val passwordError = if (passwordTouched && !passwordValid) stringResource(R.string.register_error_password_invalid) else null
    val confirmPasswordError = if (confirmPasswordTouched && !passwordsMatch) {
        stringResource(R.string.register_error_confirm_password_mismatch)
    } else {
        null
    }

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
                        text = stringResource(R.string.register_action_submit),
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
                                append(stringResource(R.string.register_action_login_prefix))
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) { append(stringResource(R.string.register_action_login)) }
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
                    text = stringResource(R.string.register_title),
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
                text = stringResource(R.string.register_section_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = stringResource(R.string.register_section_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.error != null) LastroBanner(message = uiState.error!!.resolve())
            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = name,
                onValueChange = onNameChange,
                label = stringResource(R.string.register_field_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) nameFocusedOnce = true
                        else if (nameFocusedOnce) nameTouched = true
                    },
                type = TextFieldType.Name,
                error = nameError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.register_field_email),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) emailFocusedOnce = true
                        else if (emailFocusedOnce) emailTouched = true
                    },
                type = TextFieldType.Email,
                error = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.register_field_password),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) passwordFocusedOnce = true
                        else if (passwordFocusedOnce) passwordTouched = true
                    },
                type = TextFieldType.Password,
                error = passwordError,
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
                label = stringResource(R.string.register_field_confirm_password),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) confirmPasswordFocusedOnce = true
                        else if (confirmPasswordFocusedOnce) confirmPasswordTouched = true
                    },
                type = TextFieldType.Password,
                error = confirmPasswordError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            val termsPrefix = stringResource(R.string.register_terms_prefix)
            val termsLinkTerms = stringResource(R.string.register_terms_link_terms)
            val termsMiddle = stringResource(R.string.register_terms_middle)
            val termsLinkPrivacy = stringResource(R.string.register_terms_link_privacy)
            val termsSuffix = stringResource(R.string.register_terms_suffix)

            LastroCard(
                modifier = Modifier.fillMaxWidth(),
                padding = LastroSpacing.lg,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                checked = termsAccepted,
                onCheckedChange = onTermsChange,
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(termsPrefix)
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                            append(termsLinkTerms)
                        }
                        append(termsMiddle)
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                            append(termsLinkPrivacy)
                        }
                        append(termsSuffix)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))
        }
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
        uiState = AuthUiState(error = UiText.Literal("Email já cadastrado")),
        name = "Gabriela", email = "gabriela@lastro.app", password = "Senha123!", confirmPassword = "Senha123!", termsAccepted = true,
        onNameChange = {}, onEmailChange = {}, onPasswordChange = {},
        onConfirmPasswordChange = {}, onTermsChange = {}, onRegister = {}, onBack = {},
    )
}
