package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.User
import java.math.BigDecimal
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {

    override suspend fun getMe(): Result<User> =
        Result.Error(NotImplementedError())

    override suspend fun updateMe(name: String?, monthlyIncome: BigDecimal?, savingGoalPct: Double?): Result<User> =
        Result.Error(NotImplementedError())

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        Result.Error(NotImplementedError())

    override suspend fun deleteAccount(password: String): Result<Unit> =
        Result.Error(NotImplementedError())

    override suspend fun exportData(): Result<String> =
        Result.Error(NotImplementedError())
}
