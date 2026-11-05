package com.lastro.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.usecase.CalculateBudgetBreakdownUseCase
import com.lastro.core.domain.usecase.GetTransactionsUseCase
import com.lastro.core.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    private val getTransactions: GetTransactionsUseCase,
    private val calculateBudgetBreakdown: CalculateBudgetBreakdownUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DashboardEvent>()
    val events = _events.asSharedFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val month = _uiState.value.currentMonth
            val userDeferred = async { getUserProfile() }
            val txDeferred = async { getTransactions(month.year, month.monthValue) }

            val userResult = userDeferred.await()
            val txResult = txDeferred.await()

            val user = (userResult as? Result.Success)?.data
            val userName = user?.name?.firstName() ?: ""
            val hasPartner = user?.partner != null
            val income = user?.monthlyIncome ?: BigDecimal.ZERO
            val currentUserId = user?.id ?: ""
            val currentUserInitial = userName.firstOrNull()?.uppercase() ?: ""
            val partnerInitial = user?.partner?.name?.firstName()?.firstOrNull()?.uppercase()

            val transactions = when (txResult) {
                is Result.Success -> txResult.data
                else -> emptyList()
            }

            val totalIncome = transactions
                .filter { it.direction == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
            val totalExpense = transactions
                .filter { it.direction == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }

            val breakdown = calculateBudgetBreakdown(income, transactions)

            val error = when {
                userResult is Result.Error -> userResult.message
                txResult is Result.Error -> txResult.message
                else -> null
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    userName = userName,
                    currentUserId = currentUserId,
                    currentUserInitial = currentUserInitial,
                    partnerInitial = partnerInitial,
                    hasPartner = hasPartner,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    balance = totalIncome - totalExpense,
                    budgetBreakdown = breakdown,
                    recentTransactions = transactions.sortedByDescending { tx -> tx.date }.take(4),
                    error = error,
                )
            }
        }
    }

    fun toggleBalanceVisibility() {
        _uiState.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
    }

    fun onNewTransaction() {
        viewModelScope.launch { _events.emit(DashboardEvent.NavigateToNewTransaction) }
    }

    fun onAllTransactions() {
        viewModelScope.launch { _events.emit(DashboardEvent.NavigateToAllTransactions) }
    }

    private fun String.firstName(): String = trim().split(" ").firstOrNull() ?: this
}
