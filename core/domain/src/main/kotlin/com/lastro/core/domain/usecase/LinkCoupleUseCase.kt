package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.CoupleRepository
import javax.inject.Inject

class LinkCoupleUseCase @Inject constructor(
    private val repository: CoupleRepository,
) {
    suspend operator fun invoke(code: String): Result<Unit> {
        if (code.isBlank()) {
            return Result.Error(IllegalArgumentException("Invite code must not be blank"))
        }
        return repository.joinCouple(code)
    }
}
