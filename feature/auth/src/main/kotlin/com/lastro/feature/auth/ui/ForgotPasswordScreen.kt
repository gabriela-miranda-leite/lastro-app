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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.components.ErrorBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.ForgotPasswordUiState
import com.lastro.feature.auth.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    ForgotPasswordScreenContent(
        uiState = uiState,
        email = email,
        onEmailChange = { email = it },
        onSubmit = { viewModel.submit(email) },
        onBack = onBack,
    )
}

@Composable
internal fun ForgotPasswordScreenContent(
    uiState: ForgotPasswordUiState,
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = { LastroTopBar(title = "Recuperar senha", onNavigateBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = LastroSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Text(
                text = "Informe seu email e enviaremos um link para redefinir sua senha.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.success) {
                Text(
                    text = "Se o email existir, você receberá um link de recuperação.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                ErrorBanner(
                    message = uiState.error ?: "",
                    visible = uiState.error != null,
                )

                if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

                LastroTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
                )

                Spacer(modifier = Modifier.height(LastroSpacing.xxl))

                LastroButton(
                    text = "Enviar link",
                    onClick = onSubmit,
                    loading = uiState.isLoading,
                    enabled = email.isNotBlank(),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.bottomSafe))
        }
    }
}

@Preview(name = "ForgotPassword - Default")
@Composable
private fun ForgotPasswordPreview() = LastroTheme {
    ForgotPasswordScreenContent(
        uiState = ForgotPasswordUiState(),
        email = "",
        onEmailChange = {},
        onSubmit = {},
        onBack = {},
    )
}

@Preview(name = "ForgotPassword - Carregando")
@Composable
private fun ForgotPasswordLoadingPreview() = LastroTheme {
    ForgotPasswordScreenContent(
        uiState = ForgotPasswordUiState(isLoading = true),
        email = "gabriela@lastro.app",
        onEmailChange = {},
        onSubmit = {},
        onBack = {},
    )
}

@Preview(name = "ForgotPassword - Sucesso")
@Composable
private fun ForgotPasswordSuccessPreview() = LastroTheme {
    ForgotPasswordScreenContent(
        uiState = ForgotPasswordUiState(success = true),
        email = "gabriela@lastro.app",
        onEmailChange = {},
        onSubmit = {},
        onBack = {},
    )
}

@Preview(name = "ForgotPassword - Erro")
@Composable
private fun ForgotPasswordErrorPreview() = LastroTheme {
    ForgotPasswordScreenContent(
        uiState = ForgotPasswordUiState(error = "Muitas tentativas. Aguarde alguns minutos"),
        email = "gabriela@lastro.app",
        onEmailChange = {},
        onSubmit = {},
        onBack = {},
    )
}
