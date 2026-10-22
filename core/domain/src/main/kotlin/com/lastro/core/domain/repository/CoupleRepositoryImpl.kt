package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.common.mapSuccess
import com.lastro.core.network.dto.couple.JoinRequest
import com.lastro.core.network.service.CoupleApiService
import com.lastro.core.network.util.safeApiCall
import javax.inject.Inject

class CoupleRepositoryImpl @Inject constructor(
    private val api: CoupleApiService,
) : CoupleRepository {

    override suspend fun generateInvite(): Result<String> =
        safeApiCall { api.invite() }.mapSuccess { it.code }

    override suspend fun joinCouple(code: String): Result<Unit> =
        safeApiCall { api.join(JoinRequest(code)) }.mapSuccess { Unit }

    override suspend fun unlinkCouple(): Result<Unit> =
        safeApiCall { api.unlink() }.mapSuccess { Unit }
}
