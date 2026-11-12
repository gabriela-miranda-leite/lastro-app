package com.lastro.feature.dashboard.viewmodel

import androidx.compose.ui.graphics.Color
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.ui.UiText
import com.lastro.core.ui.components.ChartAxisLabel
import com.lastro.core.ui.components.ChartPoint
import com.lastro.core.ui.components.DonutSlice
import com.lastro.core.ui.components.IconWrapVariant
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import java.time.YearMonth

fun BudgetType.displayLabel(): String = when (this) {
    BudgetType.NEEDS -> "Necessidades"
    BudgetType.WANTS -> "Desejos"
    BudgetType.SAVINGS -> "Investimentos"
}

fun BudgetType.displayColor(): Color = when (this) {
    BudgetType.NEEDS -> LastroColors.Accent
    BudgetType.WANTS -> LastroColors.Wishes
    BudgetType.SAVINGS -> LastroColors.Positive
}

fun BudgetType.displayIcon(): Int = when (this) {
    BudgetType.NEEDS -> LastroIcons.Home
    BudgetType.WANTS -> LastroIcons.Heart
    BudgetType.SAVINGS -> LastroIcons.PiggyBank
}

fun BudgetType.displayIconVariant(): IconWrapVariant = when (this) {
    BudgetType.NEEDS -> IconWrapVariant.Default
    BudgetType.WANTS -> IconWrapVariant.Negative
    BudgetType.SAVINGS -> IconWrapVariant.Positive
}

data class DashboardRecentTransaction(
    val id: String,
    val name: String,
    val meta: String,
    val amount: String,
    val isPositive: Boolean,
    val iconRes: Int,
    val iconVariant: IconWrapVariant,
    val ownerLabel: String? = null,
)

data class DashboardUiState(
    val isLoading: Boolean = true,
    val error: UiText? = null,
    val yearMonth: YearMonth = YearMonth.now(),
    val monthLabel: String = "",
    val userInitial: String = "",
    val showCoupleToggle: Boolean = false,
    val isCoupleScope: Boolean = false,
    val hasData: Boolean = false,
    val balanceLabel: String = "",
    val balanceValue: String = "",
    val isBalancePositive: Boolean = true,
    val incomeValue: String = "",
    val expenseValue: String = "",
    val donutSlices: List<DonutSlice> = emptyList(),
    val donutCenterLabel: String = "",
    val dailyBalancePoints: List<ChartPoint> = emptyList(),
    val dailyBalanceAxisLabels: List<ChartAxisLabel> = emptyList(),
    val dailyBalanceHighlightIndex: Int? = null,
    val budgetAlert: UiText? = null,
    val recentTransactions: List<DashboardRecentTransaction> = emptyList(),
)
