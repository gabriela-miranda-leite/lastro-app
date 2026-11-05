package com.lastro.feature.dashboard.ui

import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.common.toBRL
import com.lastro.core.ui.components.BottomNavItem
import com.lastro.core.ui.components.LastroAvatar
import com.lastro.core.ui.components.LastroBottomNavigation
import com.lastro.core.ui.components.LastroCard
import com.lastro.core.ui.components.LastroFab
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.viewmodel.DashboardEvent
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import com.lastro.feature.dashboard.viewmodel.DashboardViewModel
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))

private val bottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Outlined.GridView, "Dashboard"),
    BottomNavItem("Transações", Icons.Outlined.SwapVert, "Transações"),
    BottomNavItem("Fixos", Icons.Outlined.Schedule, "Fixos"),
    BottomNavItem("Perfil", Icons.Outlined.Person, "Perfil"),
)

@Composable
fun DashboardScreen(
    onNavigateToTransactions: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCouple: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = view.context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                DashboardEvent.NavigateToNewTransaction -> onNavigateToTransactions()
                DashboardEvent.NavigateToAllTransactions -> onNavigateToTransactions()
            }
        }
    }

    DashboardScreenContent(
        uiState = uiState,
        selectedNavIndex = 0,
        onNavSelected = { index ->
            when (index) {
                1 -> onNavigateToTransactions()
                3 -> onNavigateToProfile()
            }
        },
        onToggleBalanceVisibility = viewModel::toggleBalanceVisibility,
        onNewTransaction = viewModel::onNewTransaction,
        onAllTransactions = viewModel::onAllTransactions,
    )
}

@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    selectedNavIndex: Int = 0,
    onNavSelected: (Int) -> Unit = {},
    onToggleBalanceVisibility: () -> Unit = {},
    onNewTransaction: () -> Unit = {},
    onAllTransactions: () -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            LastroBottomNavigation(
                items = bottomNavItems,
                selectedIndex = selectedNavIndex,
                onItemSelected = onNavSelected,
            )
        },
        floatingActionButton = {
            LastroFab(
                onClick = onNewTransaction,
                contentDescription = "Nova transação",
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            DashboardHeader(
                userName = uiState.userName,
                currentMonth = uiState.currentMonth,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            BalanceCard(
                balance = uiState.balance,
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                isVisible = uiState.isBalanceVisible,
                onToggleVisibility = onToggleBalanceVisibility,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LastroSpacing.screenHorizontal),
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
        }
    }
}

@Composable
private fun DashboardHeader(
    userName: String,
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LastroSpacing.screenHorizontal)
            .padding(top = LastroSpacing.xl),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = if (userName.isNotEmpty()) "Olá, $userName" else "Olá",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = currentMonth.format(monthFormatter)
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        if (userName.isNotEmpty()) {
            LastroAvatar(initial = userName)
        }
    }
}

@Composable
private fun BalanceCard(
    balance: BigDecimal,
    totalIncome: BigDecimal,
    totalExpense: BigDecimal,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val balanceLabel = if (isVisible) balance.toBRL() else "R\$ ••••"
    val a11yBalance = if (isVisible) "Saldo: ${balance.toBRL()}" else "Saldo oculto"

    LastroCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Saldo do mês",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (isVisible) "Ocultar saldo" else "Exibir saldo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = balanceLabel,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { contentDescription = a11yBalance },
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        Row(modifier = Modifier.fillMaxWidth()) {
            BalanceStat(
                label = "RECEITAS",
                value = if (isVisible) totalIncome.toBRL() else "R\$ ••••",
                color = LastroColors.Positive,
                modifier = Modifier.weight(1f),
            )
            BalanceStat(
                label = "DESPESAS",
                value = if (isVisible) totalExpense.toBRL() else "R\$ ••••",
                color = LastroColors.Negative,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BalanceStat(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xs))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun android.content.Context.findActivity(): android.app.Activity? {
    var context = this
    while (context is android.content.ContextWrapper) {
        if (context is android.app.Activity) return context
        context = context.baseContext
    }
    return null
}

@Preview(name = "Dashboard - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    DashboardScreenContent(
        uiState = DashboardUiState(
            userName = "Gabi",
            totalIncome = BigDecimal("12200"),
            totalExpense = BigDecimal("5325"),
            balance = BigDecimal("6875"),
        ),
    )
}

@Preview(name = "Dashboard - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    DashboardScreenContent(
        uiState = DashboardUiState(
            userName = "Gabi",
            totalIncome = BigDecimal("12200"),
            totalExpense = BigDecimal("5325"),
            balance = BigDecimal("6875"),
        ),
    )
}

@Preview(name = "Dashboard - Saldo oculto")
@Composable
private fun PreviewHidden() = LastroTheme {
    DashboardScreenContent(
        uiState = DashboardUiState(
            userName = "Gabi",
            totalIncome = BigDecimal("12200"),
            totalExpense = BigDecimal("5325"),
            balance = BigDecimal("6875"),
            isBalanceVisible = false,
        ),
    )
}
