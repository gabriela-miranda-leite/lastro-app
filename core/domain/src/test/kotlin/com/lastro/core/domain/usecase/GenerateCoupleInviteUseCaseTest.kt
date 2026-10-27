package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.testing.repository.FakeCoupleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenerateCoupleInviteUseCaseTest {

    private lateinit var repository: FakeCoupleRepository
    private lateinit var useCase: GenerateCoupleInviteUseCase

    @Before
    fun setUp() {
        repository = FakeCoupleRepository()
        useCase = GenerateCoupleInviteUseCase(repository)
    }

    @Test
    fun `returns invite with code and expiresAt on success`() = runTest {
        val invite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z")
        repository.generateInviteResult = Result.Success(invite)

        val result = useCase()

        assertEquals(Result.Success(invite), result)
    }

    @Test
    fun `propagates repository error`() = runTest {
        repository.generateInviteResult = Result.Error(RuntimeException("network error"))

        val result = useCase()

        assertTrue(result is Result.Error)
    }
}
