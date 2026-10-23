package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.testing.repository.FakeTransactionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class CreateTransactionUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: CreateTransactionUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = CreateTransactionUseCase(repository)
    }

    @Test
    fun `returns error when amount is zero`() = runTest {
        val result = useCase(
            amount = BigDecimal.ZERO,
            direction = TransactionType.EXPENSE,
            categoryId = null,
            description = null,
            isShared = false,
            date = LocalDate.now(),
        )
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is IllegalArgumentException)
    }

    @Test
    fun `returns error when amount is negative`() = runTest {
        val result = useCase(
            amount = BigDecimal("-1"),
            direction = TransactionType.EXPENSE,
            categoryId = null,
            description = null,
            isShared = false,
            date = LocalDate.now(),
        )
        assertTrue(result is Result.Error)
    }

    @Test
    fun `delegates to repository with generated clientId`() = runTest {
        repository.createResult = Result.Success(fakeTransaction())

        useCase(
            amount = BigDecimal("100"),
            direction = TransactionType.INCOME,
            categoryId = null,
            description = null,
            isShared = false,
            date = LocalDate.now(),
        )

        assertNotNull(repository.lastCreatedClientId)
    }

    @Test
    fun `returns repository result on success`() = runTest {
        val transaction = fakeTransaction()
        repository.createResult = Result.Success(transaction)

        val result = useCase(
            amount = BigDecimal("50"),
            direction = TransactionType.EXPENSE,
            categoryId = null,
            description = null,
            isShared = false,
            date = LocalDate.now(),
        )

        assertEquals(Result.Success(transaction), result)
    }

    @Test
    fun `returns repository error on failure`() = runTest {
        repository.createResult = Result.Error(RuntimeException("network error"))

        val result = useCase(
            amount = BigDecimal("50"),
            direction = TransactionType.EXPENSE,
            categoryId = null,
            description = null,
            isShared = false,
            date = LocalDate.now(),
        )

        assertTrue(result is Result.Error)
    }

    private fun fakeTransaction() = Transaction(
        id = "server-id",
        clientId = "client-id",
        userId = "user-id",
        amount = BigDecimal("50"),
        direction = TransactionType.EXPENSE,
        category = null,
        description = null,
        isShared = false,
        date = LocalDate.now(),
        syncStatus = SyncStatus.SYNCED,
    )
}
