package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.exception.CoupleException
import com.lastro.core.domain.repository.CoupleRepository
import com.lastro.core.network.exception.ApiException
import javax.inject.Inject

class LinkCoupleUseCase @Inject constructor(
    private val repository: CoupleRepository,
) {
    suspend operator fun invoke(code: String): Result<Unit> {
        if (code.isBlank()) {
            return Result.Error(IllegalArgumentException("Invite code must not be blank"))
        }
        return when (val result = repository.joinCouple(code)) {
            is Result.Success -> result
            is Result.Error -> Result.Error(
                when (result.exception) {
                    is ApiException.Gone -> CoupleException.InviteExpired
                    is ApiException.NotFound -> CoupleException.InviteNotFound
                    is ApiException.Conflict -> CoupleException.AlreadyLinked
                    else -> result.exception
                }
            )
            is Result.Loading -> result
        }
    }
}
