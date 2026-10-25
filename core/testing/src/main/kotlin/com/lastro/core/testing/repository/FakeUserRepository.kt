package com.lastro.core.testing.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.User
import com.lastro.core.domain.repository.UserRepository
import java.math.BigDecimal
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    var getMeResult: Result<User> = Result.Success(fakeUser())
    var updateMeResult: Result<User> = Result.Success(fakeUser())
    var changePasswordResult: Result<Unit> = Result.Success(Unit)
    var deleteAccountResult: Result<Unit> = Result.Success(Unit)
    var exportDataResult: Result<String> = Result.Success("{}")

    override suspend fun getMe(): Result<User> = getMeResult

    override suspend fun updateMe(
        name: String?,
        monthlyIncome: BigDecimal?,
        savingGoalPct: Double?,
    ): Result<User> = updateMeResult

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        changePasswordResult

    override suspend fun deleteAccount(password: String): Result<Unit> = deleteAccountResult

    override suspend fun exportData(): Result<String> = exportDataResult

    companion object {
        fun fakeUser(monthlyIncome: BigDecimal? = null) = User(
            id = "user-123",
            name = "Gabriela",
            email = "gabriela@lastro.app",
            monthlyIncome = monthlyIncome,
            savingGoalPct = null,
            coupleId = null,
            partner = null,
        )
    }
}
