package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.testing.repository.FakeTransactionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteTransactionUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: DeleteTransactionUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = DeleteTransactionUseCase(repository)
    }

    @Test
    fun `returns success when repository succeeds`() = runTest {
        repository.deleteResult = Result.Success(Unit)
        val result = useCase("id")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `returns error when repository fails`() = runTest {
        repository.deleteResult = Result.Error(RuntimeException("not found"))
        val result = useCase("id")
        assertTrue(result is Result.Error)
    }
}
