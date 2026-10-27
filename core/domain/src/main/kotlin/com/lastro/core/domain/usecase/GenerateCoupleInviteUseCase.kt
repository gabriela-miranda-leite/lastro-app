package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.exception.CoupleException
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.domain.repository.CoupleRepository
import com.lastro.core.network.exception.ApiException
import javax.inject.Inject

class GenerateCoupleInviteUseCase @Inject constructor(
    private val repository: CoupleRepository,
) {
    suspend operator fun invoke(): Result<CoupleInvite> =
        when (val result = repository.generateInvite()) {
            is Result.Success -> result
            is Result.Error -> Result.Error(
                when (result.exception) {
                    is ApiException.Conflict -> CoupleException.AlreadyLinked
                    else -> result.exception
                }
            )
            is Result.Loading -> result
        }
}
