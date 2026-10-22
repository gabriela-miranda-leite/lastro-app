package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.common.mapSuccess
import com.lastro.core.domain.model.Partner
import com.lastro.core.domain.model.User
import com.lastro.core.network.dto.user.ChangePasswordRequest
import com.lastro.core.network.dto.user.DeleteAccountRequest
import com.lastro.core.network.dto.user.UpdateUserRequest
import com.lastro.core.network.dto.user.UserDto
import com.lastro.core.network.service.UserApiService
import com.lastro.core.network.util.safeApiCall
import java.math.BigDecimal
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService,
) : UserRepository {

    override suspend fun getMe(): Result<User> =
        safeApiCall { api.getMe() }.mapSuccess { it.toDomain() }

    override suspend fun updateMe(name: String?, monthlyIncome: BigDecimal?, savingGoalPct: Double?): Result<User> =
        safeApiCall {
            api.updateMe(UpdateUserRequest(name, monthlyIncome?.toPlainString(), savingGoalPct))
        }.mapSuccess { it.toDomain() }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        safeApiCall { api.changePassword(ChangePasswordRequest(currentPassword, newPassword)) }
            .mapSuccess { Unit }

    override suspend fun deleteAccount(password: String): Result<Unit> =
        safeApiCall { api.deleteAccount(DeleteAccountRequest(password)) }.mapSuccess { Unit }

    override suspend fun exportData(): Result<String> =
        safeApiCall { api.export() }

    private fun UserDto.toDomain() = User(
        id = id,
        name = name,
        email = email,
        monthlyIncome = monthlyIncome?.let { BigDecimal(it) },
        savingGoalPct = savingGoalPct,
        coupleId = coupleId,
        partner = partner?.let { Partner(it.id, it.name, it.email) },
    )
}
