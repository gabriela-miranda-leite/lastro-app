package com.lastro.feature.dashboard.viewmodel

import com.lastro.core.domain.model.BudgetBreakdown
import com.lastro.core.domain.model.Transaction
import java.math.BigDecimal
import java.time.YearMonth

data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val currentUserId: String = "",
    val currentUserInitial: String = "",
    val partnerInitial: String? = null,
    val hasPartner: Boolean = false,
    val currentMonth: YearMonth = YearMonth.now(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val balance: BigDecimal = BigDecimal.ZERO,
    val isBalanceVisible: Boolean = true,
    val budgetBreakdown: List<BudgetBreakdown> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val error: String? = null,
)
