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
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.ErrorBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.feature.auth.viewmodel.AuthEvent
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

            Text(
                text = "Entrar",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            ErrorBanner(
                message = errorMessage ?: "",
                visible = errorMessage != null,
            )

            if (errorMessage != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = email,
                onValueChange = { email = it },
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
                onValueChange = { password = it },
                label = "Senha",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            LastroButton(
                text = "Entrar",
                onClick = { viewModel.login(email, password) },
                loading = uiState.isLoading,
                enabled = email.isNotBlank() && password.isNotBlank(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroButton(
                text = "Esqueci minha senha",
                onClick = onNavigateToForgotPassword,
                variant = ButtonVariant.Ghost,
            )

            LastroButton(
                text = "Criar conta",
                onClick = onNavigateToRegister,
                variant = ButtonVariant.Outlined,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.bottomSafe))
        }
    }
}
