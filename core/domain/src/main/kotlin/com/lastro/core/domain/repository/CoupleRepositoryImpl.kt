package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import javax.inject.Inject

class CoupleRepositoryImpl @Inject constructor() : CoupleRepository {

    override suspend fun generateInvite(): Result<String> =
        Result.Error(NotImplementedError())

    override suspend fun joinCouple(code: String): Result<Unit> =
        Result.Error(NotImplementedError())

    override suspend fun unlinkCouple(): Result<Unit> =
        Result.Error(NotImplementedError())
}
