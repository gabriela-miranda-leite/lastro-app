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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.OnboardingIncomeEvent
import com.lastro.feature.auth.viewmodel.OnboardingIncomeUiState
import com.lastro.feature.auth.viewmodel.OnboardingIncomeViewModel
import java.math.BigDecimal

@Composable
fun OnboardingIncomeScreen(
    onSuccess: () -> Unit,
    viewModel: OnboardingIncomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var income by rememberSaveable { mutableStateOf("") }

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is OnboardingIncomeEvent.Success -> onSuccess()
            }
        }
    }

    OnboardingIncomeScreenContent(
        uiState = uiState,
        income = income,
        onIncomeChange = { income = it },
        onSubmit = {
            val parsed = income.replace(",", ".").toBigDecimalOrNull()
            if (parsed != null) viewModel.submit(parsed)
        },
    )
}

@Composable
internal fun OnboardingIncomeScreenContent(
    uiState: OnboardingIncomeUiState,
    income: String,
    onIncomeChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val parsedIncome = income.replace(",", ".").toBigDecimalOrNull()
    val canSubmit = parsedIncome != null && parsedIncome > BigDecimal.ZERO

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

            LastroStepIndicator(
                totalSteps = 3,
                currentStep = 2,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Text(
                text = "Sua renda mensal",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Usamos esse valor para calcular seus limites e metas de economia.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.error != null) LastroBanner(message = uiState.error)
            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = income,
                onValueChange = onIncomeChange,
                label = "Renda mensal",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Money,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            LastroButton(
                text = "Continuar",
                onClick = onSubmit,
                loading = uiState.isLoading,
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))
        }
    }
}

@Preview(name = "OnboardingIncome - Default")
@Composable
private fun OnboardingIncomePreview() = LastroTheme(darkTheme = false) {
    OnboardingIncomeScreenContent(
        uiState = OnboardingIncomeUiState(),
        income = "",
        onIncomeChange = {},
        onSubmit = {},
    )
}

@Preview(name = "OnboardingIncome - Preenchido")
@Composable
private fun OnboardingIncomeFilledPreview() = LastroTheme(darkTheme = false) {
    OnboardingIncomeScreenContent(
        uiState = OnboardingIncomeUiState(),
        income = "5000",
        onIncomeChange = {},
        onSubmit = {},
    )
}

@Preview(name = "OnboardingIncome - Erro")
@Composable
private fun OnboardingIncomeErrorPreview() = LastroTheme(darkTheme = false) {
    OnboardingIncomeScreenContent(
        uiState = OnboardingIncomeUiState(error = "Não foi possível salvar a renda. Tente novamente"),
        income = "5000",
        onIncomeChange = {},
        onSubmit = {},
    )
}
