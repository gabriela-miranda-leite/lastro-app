package com.lastro.core.testing.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.domain.repository.CoupleRepository
import javax.inject.Inject

class FakeCoupleRepository @Inject constructor() : CoupleRepository {

    var generateInviteResult: Result<CoupleInvite> = Result.Success(
        CoupleInvite(code = "LASTRO-TEST", expiresAt = "2026-10-27T20:00:00Z")
    )
    var joinCoupleResult: Result<Unit> = Result.Success(Unit)
    var unlinkCoupleResult: Result<Unit> = Result.Success(Unit)

    var lastJoinedCode: String? = null
    var unlinkCalled: Boolean = false

    override suspend fun generateInvite(): Result<CoupleInvite> = generateInviteResult

    override suspend fun joinCouple(code: String): Result<Unit> {
        lastJoinedCode = code
        return joinCoupleResult
    }

    override suspend fun unlinkCouple(): Result<Unit> {
        unlinkCalled = true
        return unlinkCoupleResult
    }
}
