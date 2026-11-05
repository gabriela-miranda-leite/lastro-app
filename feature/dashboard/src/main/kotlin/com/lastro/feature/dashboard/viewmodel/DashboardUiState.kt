package com.lastro.feature.dashboard.viewmodel

import java.math.BigDecimal
import java.time.YearMonth

data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val currentMonth: YearMonth = YearMonth.now(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val balance: BigDecimal = BigDecimal.ZERO,
    val isBalanceVisible: Boolean = true,
    val error: String? = null,
)
