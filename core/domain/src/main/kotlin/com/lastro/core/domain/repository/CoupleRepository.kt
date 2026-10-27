package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.CoupleInvite

interface CoupleRepository {
    suspend fun generateInvite(): Result<CoupleInvite>
    suspend fun joinCouple(code: String): Result<Unit>
    suspend fun unlinkCouple(): Result<Unit>
}
