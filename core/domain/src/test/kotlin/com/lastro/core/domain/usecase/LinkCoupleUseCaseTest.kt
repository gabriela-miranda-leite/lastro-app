package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.testing.repository.FakeCoupleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LinkCoupleUseCaseTest {

    private lateinit var repository: FakeCoupleRepository
    private lateinit var useCase: LinkCoupleUseCase

    @Before
    fun setUp() {
        repository = FakeCoupleRepository()
        useCase = LinkCoupleUseCase(repository)
    }

    @Test
    fun `returns error when code is blank`() = runTest {
        val result = useCase("   ")

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is IllegalArgumentException)
    }

    @Test
    fun `returns error when code is empty`() = runTest {
        val result = useCase("")

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is IllegalArgumentException)
    }

    @Test
    fun `delegates to repository with provided code`() = runTest {
        repository.joinCoupleResult = Result.Success(Unit)

        useCase("LASTRO-A3F8")

        assertEquals("LASTRO-A3F8", repository.lastJoinedCode)
    }

    @Test
    fun `returns success when repository succeeds`() = runTest {
        repository.joinCoupleResult = Result.Success(Unit)

        val result = useCase("LASTRO-A3F8")

        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `propagates repository error`() = runTest {
        repository.joinCoupleResult = Result.Error(RuntimeException("gone"))

        val result = useCase("LASTRO-A3F8")

        assertTrue(result is Result.Error)
    }
}
