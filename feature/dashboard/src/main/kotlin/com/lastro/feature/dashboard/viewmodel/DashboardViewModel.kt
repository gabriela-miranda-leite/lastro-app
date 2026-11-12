package com.lastro.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.common.toBRL
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.model.User
import com.lastro.core.domain.usecase.CalculateBudgetBreakdownUseCase
import com.lastro.core.domain.usecase.GetTransactionsUseCase
import com.lastro.core.domain.usecase.GetUserProfileUseCase
import com.lastro.core.ui.UiText
import com.lastro.core.ui.components.ChartAxisLabel
import com.lastro.core.ui.components.ChartPoint
import com.lastro.core.ui.components.DonutSlice
import com.lastro.core.ui.components.IconWrapVariant
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    private val getTransactions: GetTransactionsUseCase,
    private val calculateBudgetBreakdown: CalculateBudgetBreakdownUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(monthLabel = YearMonth.now().toLabel()))
    val uiState = _uiState.asStateFlow()

    private var currentUser: User? = null
    private var cachedTransactions: List<Transaction> = emptyList()

    init {
        load()
    }

    fun onPreviousMonth() {
        _uiState.value = _uiState.value.copy(yearMonth = _uiState.value.yearMonth.minusMonths(1))
        load()
    }

    fun onNextMonth() {
        _uiState.value = _uiState.value.copy(yearMonth = _uiState.value.yearMonth.plusMonths(1))
        load()
    }

    fun onScopeChange(isCoupleScope: Boolean) {
        _uiState.value = _uiState.value.copy(isCoupleScope = isCoupleScope)
        applyTransactions(currentUser, cachedTransactions)
    }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                monthLabel = _uiState.value.yearMonth.toLabel(),
            )

            when (val userResult = getUserProfile()) {
                is Result.Success -> currentUser = userResult.data
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = UiText.Literal(userResult.message ?: "Erro ao carregar seu perfil."),
                    )
                    return@launch
                }
                is Result.Loading -> Unit
            }

            val user = currentUser ?: return@launch
            val year = _uiState.value.yearMonth.year
            val month = _uiState.value.yearMonth.monthValue

            val ownResult = getTransactions(year, month, partnerUserId = null)
            val partnerId = user.partner?.id
            if (partnerId != null) {
                getTransactions(year, month, partnerUserId = partnerId)
            }

            when (ownResult) {
                is Result.Success -> {
                    cachedTransactions = ownResult.data
                    applyTransactions(user, cachedTransactions)
                }
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = UiText.Literal(ownResult.message ?: "Erro ao carregar suas transações."),
                )
                is Result.Loading -> Unit
            }
        }
    }

    private fun applyTransactions(user: User?, allTransactions: List<Transaction>) {
        if (user == null) return
        val isCoupleScope = _uiState.value.isCoupleScope
        val transactions = if (isCoupleScope) {
            allTransactions
        } else {
            allTransactions.filter { it.userId == user.id }
        }

        val incomeTotal = transactions.filter { it.direction == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
        val expenseTotal = transactions.filter { it.direction == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
        val balance = incomeTotal - expenseTotal

        val categoryTotals = transactions
            .filter { it.direction == TransactionType.EXPENSE }
            .groupBy { it.category?.name ?: "Outros" }
            .mapValues { (_, txs) -> txs.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount } }
            .toList()
            .sortedByDescending { (_, amount) -> amount }
        val donutSlices = categoryTotals.mapIndexed { index, (name, amount) ->
            DonutSlice(label = name, value = amount.toFloat(), color = DonutPalette[index % DonutPalette.size])
        }

        val breakdownIncome = user.monthlyIncome?.takeIf { it > BigDecimal.ZERO } ?: incomeTotal
        val breakdown = calculateBudgetBreakdown(breakdownIncome, transactions)
        val alertTarget = breakdown
            .filter { it.targetPercentage > BigDecimal.ZERO }
            .map { it to it.percentage.divide(it.targetPercentage, 4, java.math.RoundingMode.HALF_UP) }
            .filter { (_, ratio) -> ratio >= BigDecimal("0.8") }
            .maxByOrNull { (_, ratio) -> ratio }

        val yearMonth = _uiState.value.yearMonth
        val daysInMonth = yearMonth.lengthOfMonth()
        val deltaByDay = transactions
            .groupBy { it.date.dayOfMonth }
            .mapValues { (_, txs) ->
                txs.fold(BigDecimal.ZERO) { acc, tx ->
                    if (tx.direction == TransactionType.INCOME) acc + tx.amount else acc - tx.amount
                }
            }
        var runningBalance = BigDecimal.ZERO
        val dailyPoints = (1..daysInMonth).map { day ->
            runningBalance += deltaByDay[day] ?: BigDecimal.ZERO
            ChartPoint(label = day.toString(), value = runningBalance.toFloat())
        }
        val today = java.time.LocalDate.now()
        val todayIndex = if (YearMonth.from(today) == yearMonth) today.dayOfMonth - 1 else daysInMonth - 1
        val monthAbbrev = yearMonth.month.getDisplayName(TextStyle.SHORT, Locale("pt", "BR")).lowercase()
        val dailyAxisLabels = (listOf(1, 8, 15, 22).filter { it <= daysInMonth } + (todayIndex + 1))
            .distinct()
            .sorted()
            .map { day ->
                ChartAxisLabel(
                    index = day - 1,
                    text = when {
                        day - 1 == todayIndex && YearMonth.from(today) == yearMonth -> "Hoje"
                        day == 1 -> "1 $monthAbbrev"
                        else -> day.toString()
                    },
                )
            }

        val myInitial = user.name.firstOrNull()?.uppercase()
        val partnerInitial = user.partner?.name?.firstOrNull()?.uppercase()

        val recent = transactions
            .sortedByDescending { it.date }
            .take(5)
            .map { tx ->
                val categoryType = tx.category?.type
                val iconRes = if (tx.direction == TransactionType.INCOME) LastroIcons.Wallet else categoryType?.displayIcon() ?: LastroIcons.Tag
                val iconVariant = if (tx.direction == TransactionType.INCOME) IconWrapVariant.Positive else categoryType?.displayIconVariant() ?: IconWrapVariant.Negative
                DashboardRecentTransaction(
                    id = tx.id,
                    name = tx.description?.takeIf { it.isNotBlank() }
                        ?: if (tx.direction == TransactionType.INCOME) "Receita" else "Despesa",
                    meta = tx.date.toString(),
                    amount = tx.amount.toBRL(),
                    isPositive = tx.direction == TransactionType.INCOME,
                    iconRes = iconRes,
                    iconVariant = iconVariant,
                    ownerLabel = if (!isCoupleScope) {
                        null
                    } else if (tx.isShared) {
                        "G+M"
                    } else if (tx.userId == user.id) {
                        myInitial
                    } else {
                        partnerInitial
                    },
                )
            }

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = null,
            userInitial = myInitial ?: "?",
            showCoupleToggle = user.coupleId != null,
            isCoupleScope = isCoupleScope,
            hasData = transactions.isNotEmpty(),
            balanceLabel = if (isCoupleScope) "Saldo do casal" else "Saldo do mês",
            balanceValue = balance.toBRL(),
            isBalancePositive = balance >= BigDecimal.ZERO,
            incomeValue = incomeTotal.toBRL(),
            expenseValue = expenseTotal.toBRL(),
            donutSlices = donutSlices,
            donutCenterLabel = expenseTotal.toBRL(),
            dailyBalancePoints = dailyPoints,
            dailyBalanceAxisLabels = dailyAxisLabels,
            dailyBalanceHighlightIndex = todayIndex,
            budgetAlert = alertTarget?.let { (item, ratio) ->
                val pct = ratio.multiply(BigDecimal(100)).toInt()
                UiText.Literal("Você usou $pct% do orçamento de ${item.type.displayLabel()} este mês.")
            },
            recentTransactions = recent,
        )
    }
}

private val DonutPalette = listOf(
    LastroColors.Accent,
    LastroColors.Wishes,
    LastroColors.Warning,
    LastroColors.Positive,
    LastroColors.TextDim,
)

private fun YearMonth.toLabel(): String {
    val monthName = month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
        .replaceFirstChar { it.uppercase() }
    return "$monthName $year"
}
