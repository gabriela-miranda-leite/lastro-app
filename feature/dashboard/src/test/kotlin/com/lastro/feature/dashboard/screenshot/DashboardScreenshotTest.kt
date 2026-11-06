package com.lastro.feature.dashboard.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lastro.core.domain.model.BudgetBreakdown
import com.lastro.core.domain.model.BudgetStatus
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Category
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.ui.DashboardScreenContent
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

class DashboardScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    private val mockBreakdown = listOf(
        BudgetBreakdown(BudgetType.NEEDS, BigDecimal("1554"), BigDecimal("0.42"), BigDecimal("0.50"), BudgetStatus.UNDER),
        BudgetBreakdown(BudgetType.WANTS, BigDecimal("1036"), BigDecimal("0.28"), BigDecimal("0.30"), BudgetStatus.UNDER),
        BudgetBreakdown(BudgetType.INVESTMENTS, BigDecimal("665"), BigDecimal("0.18"), BigDecimal("0.10"), BudgetStatus.OVER),
        BudgetBreakdown(BudgetType.SAVINGS, BigDecimal("444"), BigDecimal("0.12"), BigDecimal("0.10"), BudgetStatus.MET),
    )

    private val catLazer = Category(id = "1", name = "Lazer", type = BudgetType.WANTS, icon = "❤️", color = "#FF6B6B")
    private val catReceita = Category(id = "2", name = "Receita", type = BudgetType.SAVINGS, icon = "💰", color = "#4ECBA8")
    private val catNecessidade = Category(id = "3", name = "Necessidade", type = BudgetType.NEEDS, icon = "🏠", color = "#6B7CFF")

    private val today = LocalDate.of(2025, 10, 15)

    private val mockTransactions = listOf(
        Transaction(
            id = "t1", clientId = "c1", userId = "u1",
            amount = BigDecimal("45.90"), direction = TransactionType.EXPENSE,
            category = catLazer, description = "iFood",
            isShared = false, date = today, syncStatus = SyncStatus.SYNCED,
        ),
        Transaction(
            id = "t2", clientId = "c2", userId = "u1",
            amount = BigDecimal("5000"), direction = TransactionType.INCOME,
            category = catReceita, description = "Salário Gabi",
            isShared = false, date = today.minusDays(1), syncStatus = SyncStatus.SYNCED,
        ),
        Transaction(
            id = "t3", clientId = "c3", userId = "u2",
            amount = BigDecimal("7200"), direction = TransactionType.INCOME,
            category = catReceita, description = "Salário Marlon",
            isShared = false, date = today.minusDays(1), syncStatus = SyncStatus.SYNCED,
        ),
        Transaction(
            id = "t4", clientId = "c4", userId = "u1",
            amount = BigDecimal("1800"), direction = TransactionType.EXPENSE,
            category = catNecessidade, description = "Aluguel",
            isShared = true, date = today.minusDays(2), syncStatus = SyncStatus.SYNCED,
        ),
    )

    private val filledState = DashboardUiState(
        userName = "Gabi",
        currentUserId = "u1",
        currentUserInitial = "G",
        partnerInitial = "M",
        hasPartner = true,
        currentMonth = YearMonth.of(2025, 10),
        totalIncome = BigDecimal("12200"),
        totalExpense = BigDecimal("5325"),
        balance = BigDecimal("6875"),
        budgetBreakdown = mockBreakdown,
        recentTransactions = mockTransactions,
    )

    @Test fun default_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) { DashboardScreenContent(uiState = DashboardUiState()) }
    }

    @Test fun default_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) { DashboardScreenContent(uiState = DashboardUiState()) }
    }

    @Test fun filled_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) { DashboardScreenContent(uiState = filledState) }
    }

    @Test fun filled_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) { DashboardScreenContent(uiState = filledState) }
    }

    @Test fun balance_hidden_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) { DashboardScreenContent(uiState = filledState.copy(isBalanceVisible = false)) }
    }

    @Test fun loading_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) { DashboardScreenContent(uiState = DashboardUiState(isLoading = true)) }
    }
}
