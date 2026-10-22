package com.lastro.core.domain.usecase

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(
        amount: BigDecimal,
        direction: TransactionType,
        categoryId: String?,
        description: String?,
        isShared: Boolean,
        date: LocalDate,
    ): Result<Transaction> {
        if (amount <= BigDecimal.ZERO) {
            return Result.Error(IllegalArgumentException("Amount must be greater than zero"))
        }
        return repository.createTransaction(
            clientId = UUID.randomUUID().toString(),
            amount = amount,
            direction = direction,
            categoryId = categoryId,
            description = description,
            isShared = isShared,
            date = date,
        )
    }
}
