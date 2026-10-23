package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        direction: TransactionType? = null,
        partnerUserId: String? = null,
    ): Result<List<Transaction>> = repository.getTransactions(year, month, direction, partnerUserId)
}
