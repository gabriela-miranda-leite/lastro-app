package com.lastro.feature.auth.ui

import android.view.WindowManager
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroLogoMark
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.AuthEvent
import com.lastro.feature.auth.viewmodel.AuthUiState
import com.lastro.feature.auth.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.LoginSuccess -> onLoginSuccess()
                else -> Unit
            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLogin = { viewModel.login(email, password) },
        onForgotPassword = onNavigateToForgotPassword,
        onRegister = onNavigateToRegister,
    )
}

@Composable
internal fun LoginScreenContent(
    uiState: AuthUiState,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
) {
    val errorMessage = when (uiState.error) {
        ApiException.Unauthorized.message -> "Email ou senha incorretos"
        ApiException.TooManyRequests.message -> "Muitas tentativas. Aguarde alguns minutos"
        else -> uiState.error
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.sectionTopLarge))

            LastroLogoMark(size = 72.dp)

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = "Lastro",
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Controle financeiro pessoal",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.sectionTop))

            if (errorMessage != null) LastroBanner(message = errorMessage)
            if (errorMessage != null) Spacer(modifier = Modifier.height(LastroSpacing.md))
            LastroTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Email,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = "Esqueci a senha",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable(onClick = onForgotPassword)
                        .padding(vertical = LastroSpacing.sm),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xl))

            LastroButton(
                text = "Entrar",
                onClick = onLogin,
                loading = uiState.isLoading,
                enabled = email.isNotBlank() && password.isNotBlank(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline),
                )
                Text(
                    text = "  ou  ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xl))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRegister),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Não tem conta? ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) { append("Criar conta") }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Seus dados são criptografados e protegidos",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = LastroSpacing.xxl),
            )
        }
    }
}

@Preview(name = "Login - Default")
@Composable
private fun LoginScreenPreview() = LastroTheme(darkTheme = false) {
    LoginScreenContent(
        uiState = AuthUiState(), email = "", password = "",
        onEmailChange = {}, onPasswordChange = {}, onLogin = {},
        onForgotPassword = {}, onRegister = {},
    )
}

@Preview(name = "Login - Preenchido")
@Composable
private fun LoginScreenFilledPreview() = LastroTheme(darkTheme = false) {
    LoginScreenContent(
        uiState = AuthUiState(), email = "gabriela@lastro.app", password = "senha123",
        onEmailChange = {}, onPasswordChange = {}, onLogin = {},
        onForgotPassword = {}, onRegister = {},
    )
}

@Preview(name = "Login - Carregando")
@Composable
private fun LoginScreenLoadingPreview() = LastroTheme(darkTheme = false) {
    LoginScreenContent(
        uiState = AuthUiState(isLoading = true), email = "gabriela@lastro.app", password = "senha123",
        onEmailChange = {}, onPasswordChange = {}, onLogin = {},
        onForgotPassword = {}, onRegister = {},
    )
}

@Preview(name = "Login - Erro 401")
@Composable
private fun LoginScreenErrorPreview() = LastroTheme(darkTheme = false) {
    LoginScreenContent(
        uiState = AuthUiState(error = ApiException.Unauthorized.message),
        email = "gabriela@lastro.app", password = "errada",
        onEmailChange = {}, onPasswordChange = {}, onLogin = {},
        onForgotPassword = {}, onRegister = {},
    )
}
