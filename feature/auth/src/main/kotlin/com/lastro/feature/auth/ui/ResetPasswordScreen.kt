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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.R as CoreR
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.FeedbackScreenVariant
import com.lastro.core.ui.components.LastroBackButton
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroFeedbackScreen
import com.lastro.core.ui.components.LastroPasswordRequirements
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.PasswordRequirement
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroIconWrapShape
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSecurityNoteTextStyle
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.R
import com.lastro.feature.auth.viewmodel.ResetPasswordUiState
import com.lastro.feature.auth.viewmodel.ResetPasswordViewModel

@Composable
fun ResetPasswordScreen(
    token: String,
    onSuccess: () -> Unit,
    onRequestNewLink: () -> Unit,
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

    ResetPasswordScreenContent(
        uiState = uiState,
        newPassword = newPassword,
        confirmPassword = confirmPassword,
        onNewPasswordChange = { newPassword = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onSubmit = { viewModel.submit(token, newPassword) },
        onSuccessContinue = onSuccess,
        onRequestNewLink = onRequestNewLink,
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
    onSuccessContinue: () -> Unit,
    onRequestNewLink: () -> Unit,
    onBack: () -> Unit,
) {
    if (uiState.success) {
        LastroFeedbackScreen(
            title = stringResource(R.string.reset_password_success_title),
            description = stringResource(R.string.reset_password_success_desc),
            variant = FeedbackScreenVariant.Success,
            primaryActionLabel = stringResource(R.string.reset_password_action_login),
            onPrimaryAction = onSuccessContinue,
            primaryActionVariant = ButtonVariant.Ghost,
        )
        return
    }

    if (uiState.invalidToken) {
        LastroFeedbackScreen(
            title = stringResource(R.string.reset_password_invalid_title),
            description = stringResource(R.string.reset_password_invalid_desc),
            variant = FeedbackScreenVariant.Error,
            primaryActionLabel = stringResource(R.string.reset_password_action_request_new),
            onPrimaryAction = onRequestNewLink,
            footer = {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.reset_password_remembered_prefix))
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            ) { append(stringResource(R.string.login_action_submit)) }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable(onClick = onBack),
                    )
                }
            },
        )
        return
    }

    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasNumber = newPassword.any { it.isDigit() || !it.isLetterOrDigit() }
    val passwordsMatch = newPassword.isNotEmpty() && newPassword == confirmPassword
    val canSubmit = hasMinLength && hasUppercase && hasNumber && passwordsMatch

    var confirmPasswordFocusedOnce by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordTouched by rememberSaveable { mutableStateOf(false) }
    val confirmPasswordError = if (confirmPasswordTouched && confirmPassword.isNotEmpty() && !passwordsMatch) {
        stringResource(R.string.reset_password_error_mismatch)
    } else {
        null
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
            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroBackButton(onClick = onBack)

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Box(
                modifier = Modifier
                    .size(LastroIconWrapSize.lg)
                    .background(LastroColors.Accent.copy(alpha = 0.12f), LastroIconWrapShape)
                    .border(1.dp, LastroColors.Accent.copy(alpha = 0.25f), LastroIconWrapShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = CoreR.drawable.ic_lock),
                    contentDescription = null,
                    tint = LastroColors.Accent,
                    modifier = Modifier.size(LastroIconSize.lg),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = stringResource(R.string.reset_password_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = stringResource(R.string.reset_password_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyMedium.fontSize * 1.6f,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.sectionTop))

            LastroTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = stringResource(R.string.reset_password_field_new),
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Password,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.sm))
            LastroPasswordRequirements(
                requirements = listOf(
                    PasswordRequirement(stringResource(R.string.reset_password_requirement_min_length), met = hasMinLength),
                    PasswordRequirement(stringResource(R.string.reset_password_requirement_uppercase), met = hasUppercase),
                    PasswordRequirement(stringResource(R.string.reset_password_requirement_number), met = hasNumber),
                ),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.reset_password_field_confirm),
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

            Spacer(modifier = Modifier.height(LastroSpacing.xl))

            LastroButton(
                text = stringResource(R.string.reset_password_action_submit),
                onClick = onSubmit,
                loading = uiState.isLoading,
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.reset_password_security_note),
                style = LastroSecurityNoteTextStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = LastroSpacing.xxl),
            )
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
        onSuccessContinue = {},
        onRequestNewLink = {},
        onBack = {},
    )
}

@Preview(name = "ResetPassword - Senha fraca")
@Composable
private fun ResetPasswordWeakPreview() = LastroTheme(darkTheme = false) {
    ResetPasswordScreenContent(
        uiState = ResetPasswordUiState(),
        newPassword = "abc",
        confirmPassword = "",
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onSubmit = {},
        onSuccessContinue = {},
        onRequestNewLink = {},
        onBack = {},
    )
}

@Preview(name = "ResetPassword - Sucesso")
@Composable
private fun ResetPasswordSuccessPreview() = LastroTheme(darkTheme = false) {
    ResetPasswordScreenContent(
        uiState = ResetPasswordUiState(success = true),
        newPassword = "Senha123!",
        confirmPassword = "Senha123!",
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onSubmit = {},
        onSuccessContinue = {},
        onRequestNewLink = {},
        onBack = {},
    )
}

@Preview(name = "ResetPassword - Link expirado")
@Composable
private fun ResetPasswordInvalidTokenPreview() = LastroTheme(darkTheme = false) {
    ResetPasswordScreenContent(
        uiState = ResetPasswordUiState(invalidToken = true),
        newPassword = "",
        confirmPassword = "",
        onNewPasswordChange = {},
        onConfirmPasswordChange = {},
        onSubmit = {},
        onSuccessContinue = {},
        onRequestNewLink = {},
        onBack = {},
    )
}
