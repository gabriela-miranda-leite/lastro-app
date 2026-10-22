package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Result<Unit> {
        if (newPassword != confirmPassword) {
            return Result.Error(IllegalArgumentException("Passwords do not match"))
        }
        if (newPassword.length < 8) {
            return Result.Error(IllegalArgumentException("Password must be at least 8 characters"))
        }
        if (!newPassword.any { it.isLetter() } || !newPassword.any { it.isDigit() }) {
            return Result.Error(IllegalArgumentException("Password must contain letters and numbers"))
        }
        return repository.changePassword(currentPassword, newPassword)
    }
}
