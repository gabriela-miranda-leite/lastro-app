package com.lastro.feature.auth.ui

import android.view.WindowManager
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
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
import androidx.compose.ui.res.painterResource
import com.lastro.core.ui.R
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.LastroBackButton
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroColors
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

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(LastroColors.Accent.copy(alpha = 0.12f))
                    .border(1.dp, LastroColors.Accent.copy(alpha = 0.25f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    tint = LastroColors.Accent,
                    modifier = Modifier.size(28.dp),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = "Esqueci a senha",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            Text(
                text = "Informe o email cadastrado e enviaremos um link para redefinir sua senha.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.success) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LastroColors.Positive.copy(alpha = 0.10f))
                        .border(1.dp, LastroColors.Positive.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .padding(LastroSpacing.xxl),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(LastroColors.Positive.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = LastroColors.Positive,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(LastroSpacing.md))
                        Text(
                            text = "Email enviado",
                            style = MaterialTheme.typography.titleMedium,
                            color = LastroColors.Positive,
                        )
                        Spacer(modifier = Modifier.height(LastroSpacing.xs))
                        Text(
                            text = "Se esse email estiver cadastrado, você receberá um link para redefinir sua senha em breve.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            } else {
                if (uiState.error != null) LastroBanner(message = uiState.error)
                if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

                LastroTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    modifier = Modifier.fillMaxWidth(),
                    type = TextFieldType.Email,
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

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Lembrou a senha? ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) { append("Voltar ao login") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable(onClick = onBack),
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

@Preview(name = "ForgotPassword - Default")
@Composable
private fun ForgotPasswordPreview() = LastroTheme(darkTheme = false) {
    ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(), email = "", onEmailChange = {}, onSubmit = {}, onBack = {})
}

@Preview(name = "ForgotPassword - Carregando")
@Composable
private fun ForgotPasswordLoadingPreview() = LastroTheme(darkTheme = false) {
    ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(isLoading = true), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {})
}

@Preview(name = "ForgotPassword - Sucesso")
@Composable
private fun ForgotPasswordSuccessPreview() = LastroTheme(darkTheme = false) {
    ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(success = true), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {})
}

@Preview(name = "ForgotPassword - Erro")
@Composable
private fun ForgotPasswordErrorPreview() = LastroTheme(darkTheme = false) {
    ForgotPasswordScreenContent(uiState = ForgotPasswordUiState(error = "Muitas tentativas. Aguarde alguns minutos"), email = "gabriela@lastro.app", onEmailChange = {}, onSubmit = {}, onBack = {})
}
