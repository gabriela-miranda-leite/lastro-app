package com.lastro.feature.auth.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.ErrorBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.feature.auth.viewmodel.AuthEvent
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

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.RegisterSuccess -> onRegisterSuccess()
                else -> Unit
            }
        }
    }

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
            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Text(
                text = "Bem-vindo ao Lastro",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            ErrorBanner(
                message = uiState.error ?: "",
                visible = uiState.error != null,
            )

            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nome",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

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
                text = "Cadastrar",
                onClick = { viewModel.register(email, password, name) },
                loading = uiState.isLoading,
                enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
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
