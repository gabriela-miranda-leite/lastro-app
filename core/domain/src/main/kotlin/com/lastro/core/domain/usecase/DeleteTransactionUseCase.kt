package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> =
        repository.deleteTransaction(id)
}
