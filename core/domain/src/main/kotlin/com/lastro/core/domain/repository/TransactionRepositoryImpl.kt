package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor() : TransactionRepository {

    override suspend fun getTransactions(year: Int, month: Int, direction: TransactionType?): Result<List<Transaction>> =
        Result.Success(emptyList())

    override suspend fun getTransaction(id: String): Result<Transaction> =
        Result.Error(NotImplementedError())

    override suspend fun createTransaction(
        clientId: String,
        amount: BigDecimal,
        direction: TransactionType,
        categoryId: String?,
        description: String?,
        isShared: Boolean,
        date: LocalDate,
    ): Result<Transaction> = Result.Error(NotImplementedError())

    override suspend fun updateTransaction(
        id: String,
        amount: BigDecimal?,
        categoryId: String?,
        description: String?,
        isShared: Boolean?,
        date: LocalDate?,
    ): Result<Transaction> = Result.Error(NotImplementedError())

    override suspend fun deleteTransaction(id: String): Result<Unit> =
        Result.Success(Unit)
}
