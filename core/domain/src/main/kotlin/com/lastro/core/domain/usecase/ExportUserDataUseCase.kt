package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.UserRepository
import javax.inject.Inject

class ExportUserDataUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): Result<String> = repository.exportData()
}
