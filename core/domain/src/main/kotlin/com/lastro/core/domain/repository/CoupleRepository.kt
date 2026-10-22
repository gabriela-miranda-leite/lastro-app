package com.lastro.core.domain.repository

import com.lastro.core.common.Result

interface CoupleRepository {
    suspend fun generateInvite(): Result<String>
    suspend fun joinCouple(code: String): Result<Unit>
    suspend fun unlinkCouple(): Result<Unit>
}
