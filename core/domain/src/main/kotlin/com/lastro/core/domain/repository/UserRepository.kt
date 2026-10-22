package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.User

interface UserRepository {
    suspend fun getMe(): Result<User>
    suspend fun updateMe(name: String?, monthlyIncome: java.math.BigDecimal?, savingGoalPct: Double?): Result<User>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun deleteAccount(password: String): Result<Unit>
    suspend fun exportData(): Result<String>
}
