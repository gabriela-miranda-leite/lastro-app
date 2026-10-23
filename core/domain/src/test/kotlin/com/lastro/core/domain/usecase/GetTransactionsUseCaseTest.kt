package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.testing.repository.FakeTransactionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class GetTransactionsUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: GetTransactionsUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = GetTransactionsUseCase(repository)
    }

    @Test
    fun `returns transactions from repository`() = runTest {
        repository.transactions = listOf(fakeTransaction())

        val result = useCase(year = 2026, month = 10)

        assertEquals(Result.Success(repository.transactions), result)
    }

    @Test
    fun `returns empty list when repository has no transactions`() = runTest {
        val result = useCase(year = 2026, month = 10)
        assertEquals(Result.Success(emptyList<Transaction>()), result)
    }

    private fun fakeTransaction() = Transaction(
        id = "id",
        clientId = "client-id",
        userId = "user-id",
        amount = BigDecimal("100"),
        direction = TransactionType.INCOME,
        category = null,
        description = null,
        isShared = false,
        date = LocalDate.of(2026, 10, 1),
        syncStatus = SyncStatus.SYNCED,
    )
}
