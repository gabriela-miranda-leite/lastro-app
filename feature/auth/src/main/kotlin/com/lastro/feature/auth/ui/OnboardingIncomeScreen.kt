package com.lastro.feature.auth.ui

import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.R
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroStepIndicator
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.auth.viewmodel.OnboardingIncomeEvent
import com.lastro.feature.auth.viewmodel.OnboardingIncomeUiState
import com.lastro.feature.auth.viewmodel.OnboardingIncomeViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OnboardingIncomeScreen(
    onSuccess: () -> Unit,
    viewModel: OnboardingIncomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var income by rememberSaveable { mutableStateOf("") }
    var savingGoalPct by rememberSaveable { mutableFloatStateOf(0.20f) }

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
        savingGoalPct = savingGoalPct,
        onIncomeChange = { income = it },
        onSavingGoalChange = { savingGoalPct = it },
        onSubmit = {
            val parsed = income.replace(",", ".").toBigDecimalOrNull()
            if (parsed != null) viewModel.submit(parsed, savingGoalPct.toDouble())
        },
        onSkip = { viewModel.skip() },
    )
}

@Composable
internal fun OnboardingIncomeScreenContent(
    uiState: OnboardingIncomeUiState,
    income: String,
    savingGoalPct: Float,
    onIncomeChange: (String) -> Unit,
    onSavingGoalChange: (Float) -> Unit,
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
) {
    val parsedIncome = income.replace(",", ".").toBigDecimalOrNull() ?: BigDecimal.ZERO
    val canSubmit = parsedIncome > BigDecimal.ZERO
    val goalAmount = parsedIncome.multiply(BigDecimal(savingGoalPct.toDouble()))
    val goalFormatted = NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }.format(goalAmount)
    val goalPctInt = (savingGoalPct * 100).toInt()

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = LastroSpacing.screenHorizontal)
                    .padding(top = LastroSpacing.md, bottom = LastroSpacing.bottomSafe),
            ) {
                LastroButton(
                    text = "Continuar",
                    onClick = onSubmit,
                    loading = uiState.isLoading,
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(LastroSpacing.sm))
                LastroButton(
                    text = "Pular por agora",
                    onClick = onSkip,
                    enabled = !uiState.isLoading,
                    variant = ButtonVariant.Outlined,
                    modifier = Modifier.fillMaxWidth(),
                )
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

            LastroStepIndicator(
                totalSteps = 3,
                currentStep = 3,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(LastroColors.Accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chart_growth),
                    contentDescription = null,
                    tint = LastroColors.Accent,
                    modifier = Modifier.size(32.dp),
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = "Passo 3 de 3",
                style = MaterialTheme.typography.labelSmall,
                color = LastroColors.Accent,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Configure seu perfil financeiro",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xs))
            Text(
                text = "Essas informações ajudam o Lastro a calcular seus limites de gasto e meta de poupança.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))

            if (uiState.error != null) LastroBanner(message = uiState.error)
            if (uiState.error != null) Spacer(modifier = Modifier.height(LastroSpacing.md))

            LastroTextField(
                value = income,
                onValueChange = onIncomeChange,
                label = "Renda mensal líquida",
                modifier = Modifier.fillMaxWidth(),
                type = TextFieldType.Money,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(LastroSpacing.xxl))

            Text(
                text = "Meta de poupança",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.sm))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                    .padding(horizontal = LastroSpacing.md, vertical = LastroSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$goalPctInt%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LastroColors.Accent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "R$ $goalFormatted",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "por mês",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Slider(
                value = savingGoalPct,
                onValueChange = onSavingGoalChange,
                valueRange = 0f..0.5f,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = LastroColors.Accent,
                    activeTrackColor = LastroColors.Accent,
                    inactiveTrackColor = LastroColors.Accent.copy(alpha = 0.24f),
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "0%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "50%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.md))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .padding(LastroSpacing.md),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info_circle),
                    contentDescription = null,
                    tint = LastroColors.Accent,
                    modifier = Modifier.size(16.dp).padding(top = 1.dp),
                )
                Spacer(modifier = Modifier.size(LastroSpacing.sm))
                Text(
                    text = "Regra 50/30/20 — sugerimos guardar pelo menos 20% da renda: 50% para necessidades, 30% para lazer.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

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
        savingGoalPct = 0.20f,
        onIncomeChange = {},
        onSavingGoalChange = {},
        onSubmit = {},
        onSkip = {},
    )
}

@Preview(name = "OnboardingIncome - Preenchido")
@Composable
private fun OnboardingIncomeFilledPreview() = LastroTheme(darkTheme = false) {
    OnboardingIncomeScreenContent(
        uiState = OnboardingIncomeUiState(),
        income = "5000",
        savingGoalPct = 0.20f,
        onIncomeChange = {},
        onSavingGoalChange = {},
        onSubmit = {},
        onSkip = {},
    )
}

@Preview(name = "OnboardingIncome - Erro")
@Composable
private fun OnboardingIncomeErrorPreview() = LastroTheme(darkTheme = false) {
    OnboardingIncomeScreenContent(
        uiState = OnboardingIncomeUiState(error = "Não foi possível salvar. Tente novamente"),
        income = "5000",
        savingGoalPct = 0.20f,
        onIncomeChange = {},
        onSavingGoalChange = {},
        onSubmit = {},
        onSkip = {},
    )
}
