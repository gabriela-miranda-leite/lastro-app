package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.User
import com.lastro.core.domain.repository.UserRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        name: String?,
        monthlyIncome: BigDecimal?,
        savingGoalPct: Double?,
    ): Result<User> {
        if (name != null && name.isBlank()) {
            return Result.Error(IllegalArgumentException("Name must not be blank"))
        }
        if (monthlyIncome != null && monthlyIncome <= BigDecimal.ZERO) {
            return Result.Error(IllegalArgumentException("Monthly income must be greater than zero"))
        }
        if (savingGoalPct != null && (savingGoalPct < 0.0 || savingGoalPct > 1.0)) {
            return Result.Error(IllegalArgumentException("Saving goal must be between 0 and 1"))
        }
        return repository.updateMe(name, monthlyIncome, savingGoalPct)
    }
}
