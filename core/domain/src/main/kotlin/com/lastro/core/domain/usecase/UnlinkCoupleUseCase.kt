package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.User
import com.lastro.core.domain.repository.CoupleRepository
import com.lastro.core.domain.repository.UserRepository
import javax.inject.Inject

class UnlinkCoupleUseCase @Inject constructor(
    private val coupleRepository: CoupleRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        val user = userRepository.getMe()
        if (user is com.lastro.core.common.Result.Success && user.data.coupleId == null) {
            return Result.Error(IllegalStateException("User is not linked to a couple"))
        }
        return coupleRepository.unlinkCouple()
    }
}
