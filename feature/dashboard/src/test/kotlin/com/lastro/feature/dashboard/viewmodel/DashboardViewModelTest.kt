package com.lastro.feature.dashboard.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Category
import com.lastro.core.domain.model.Partner
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.model.User
import com.lastro.core.domain.usecase.CalculateBudgetBreakdownUseCase
import com.lastro.core.domain.usecase.GetTransactionsUseCase
import com.lastro.core.domain.usecase.GetUserProfileUseCase
import com.lastro.core.testing.repository.FakeTransactionRepository
import com.lastro.core.testing.repository.FakeUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var userRepository: FakeUserRepository
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var viewModel: DashboardViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = FakeUserRepository()
        transactionRepository = FakeTransactionRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(): DashboardViewModel = DashboardViewModel(
        getUserProfile = GetUserProfileUseCase(userRepository),
        getTransactions = GetTransactionsUseCase(transactionRepository),
        calculateBudgetBreakdown = CalculateBudgetBreakdownUseCase(),
    )

    private fun needsCategory() = Category(id = "cat-needs", name = "Aluguel", type = BudgetType.NEEDS, icon = null, color = null)
    private fun wantsCategory() = Category(id = "cat-wants", name = "Lazer", type = BudgetType.WANTS, icon = null, color = null)

    private fun transaction(
        id: String,
        direction: TransactionType,
        amount: String,
        date: LocalDate,
        userId: String = "user-123",
        category: Category? = null,
        description: String? = null,
    ) = Transaction(
        id = id,
        clientId = id,
        userId = userId,
        amount = BigDecimal(amount),
        direction = direction,
        category = category,
        description = description,
        isShared = false,
        date = date,
        syncStatus = SyncStatus.SYNCED,
    )

    @Test
    fun `loads user and transactions on init`() = runTest {
        userRepository.getMeResult = Result.Success(
            FakeUserRepository.fakeUser(monthlyIncome = BigDecimal("5000")),
        )
        transactionRepository.transactions = listOf(
            transaction("1", TransactionType.INCOME, "5000", LocalDate.of(2026, 10, 1)),
            transaction("2", TransactionType.EXPENSE, "1800", LocalDate.of(2026, 10, 5), category = needsCategory()),
        )

        viewModel = buildViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.hasData)
        assertEquals("G", state.userInitial)
        assertEquals(2, state.recentTransactions.size)
    }

    @Test
    fun `empty month shows empty state`() = runTest {
        userRepository.getMeResult = Result.Success(FakeUserRepository.fakeUser())
        transactionRepository.transactions = emptyList()

        viewModel = buildViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.hasData)
        assertNull(state.error)
    }

    @Test
    fun `user profile error surfaces error state`() = runTest {
        userRepository.getMeResult = Result.Error(RuntimeException("offline"))

        viewModel = buildViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `couple toggle hidden without partner`() = runTest {
        userRepository.getMeResult = Result.Success(FakeUserRepository.fakeUser())
        transactionRepository.transactions = emptyList()

        viewModel = buildViewModel()

        assertFalse(viewModel.uiState.value.showCoupleToggle)
    }

    @Test
    fun `couple toggle visible with partner`() = runTest {
        val user = FakeUserRepository.fakeUser().copy(
            coupleId = "couple-1",
            partner = Partner(id = "partner-1", name = "Marlon", email = "marlon@lastro.app"),
        )
        userRepository.getMeResult = Result.Success(user)
        transactionRepository.transactions = listOf(
            transaction("1", TransactionType.INCOME, "5000", LocalDate.of(2026, 10, 1), userId = "user-123"),
            transaction("2", TransactionType.INCOME, "7200", LocalDate.of(2026, 10, 2), userId = "partner-1"),
        )

        viewModel = buildViewModel()

        assertTrue(viewModel.uiState.value.showCoupleToggle)
        assertEquals(1, viewModel.uiState.value.recentTransactions.size)

        viewModel.onScopeChange(true)

        assertEquals(2, viewModel.uiState.value.recentTransactions.size)
        assertEquals("Saldo do casal", viewModel.uiState.value.balanceLabel)
    }

    @Test
    fun `switching months reloads data`() = runTest {
        userRepository.getMeResult = Result.Success(FakeUserRepository.fakeUser(monthlyIncome = BigDecimal("5000")))
        transactionRepository.transactions = listOf(
            transaction("1", TransactionType.EXPENSE, "500", LocalDate.of(2026, 10, 10), category = wantsCategory()),
        )

        viewModel = buildViewModel()
        val initialMonth = viewModel.uiState.value.yearMonth

        viewModel.onNextMonth()

        assertEquals(initialMonth.plusMonths(1), viewModel.uiState.value.yearMonth)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `budget alert shown when a category is over target`() = runTest {
        userRepository.getMeResult = Result.Success(FakeUserRepository.fakeUser(monthlyIncome = BigDecimal("1000")))
        transactionRepository.transactions = listOf(
            transaction("1", TransactionType.EXPENSE, "900", LocalDate.of(2026, 10, 10), category = wantsCategory()),
        )

        viewModel = buildViewModel()

        assertNotNull(viewModel.uiState.value.budgetAlert)
    }
}
