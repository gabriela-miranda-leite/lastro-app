package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.testing.repository.FakeTransactionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class UpdateTransactionUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: UpdateTransactionUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = UpdateTransactionUseCase(repository)
    }

    @Test
    fun `returns error when amount is zero`() = runTest {
        val result = useCase(id = "id", amount = BigDecimal.ZERO, categoryId = null, description = null, isShared = null, date = null)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is IllegalArgumentException)
    }

    @Test
    fun `returns error when amount is negative`() = runTest {
        val result = useCase(id = "id", amount = BigDecimal("-1"), categoryId = null, description = null, isShared = null, date = null)
        assertTrue(result is Result.Error)
    }

    @Test
    fun `allows null amount without validation error`() = runTest {
        repository.updateResult = Result.Success(fakeTransaction())
        val result = useCase(id = "id", amount = null, categoryId = null, description = null, isShared = null, date = null)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `delegates to repository on valid input`() = runTest {
        val transaction = fakeTransaction()
        repository.updateResult = Result.Success(transaction)

        val result = useCase(id = "id", amount = BigDecimal("100"), categoryId = null, description = null, isShared = null, date = null)

        assertEquals(Result.Success(transaction), result)
    }

    private fun fakeTransaction() = Transaction(
        id = "id",
        clientId = "client-id",
        userId = "user-id",
        amount = BigDecimal("100"),
        direction = TransactionType.EXPENSE,
        category = null,
        description = null,
        isShared = false,
        date = LocalDate.now(),
        syncStatus = SyncStatus.SYNCED,
    )
}
