package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(
        id: String,
        amount: BigDecimal?,
        categoryId: String?,
        description: String?,
        isShared: Boolean?,
        date: LocalDate?,
    ): Result<Transaction> {
        if (amount != null && amount <= BigDecimal.ZERO) {
            return Result.Error(IllegalArgumentException("Amount must be greater than zero"))
        }
        return repository.updateTransaction(
            id = id,
            amount = amount,
            categoryId = categoryId,
            description = description,
            isShared = isShared,
            date = date,
        )
    }
}
