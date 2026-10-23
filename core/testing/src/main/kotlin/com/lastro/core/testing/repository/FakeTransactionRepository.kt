package com.lastro.core.testing.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate

class FakeTransactionRepository : TransactionRepository {

    var transactions: List<Transaction> = emptyList()
    var createResult: Result<Transaction>? = null
    var updateResult: Result<Transaction>? = null
    var deleteResult: Result<Unit> = Result.Success(Unit)

    var lastCreatedClientId: String? = null

    override suspend fun getTransactions(
        year: Int,
        month: Int,
        direction: TransactionType?,
        partnerUserId: String?,
    ): Result<List<Transaction>> = Result.Success(transactions)

    override suspend fun getTransaction(id: String): Result<Transaction> {
        val tx = transactions.find { it.id == id }
        return if (tx != null) Result.Success(tx) else Result.Error(NoSuchElementException())
    }

    override suspend fun createTransaction(
        clientId: String,
        amount: BigDecimal,
        direction: TransactionType,
        categoryId: String?,
        description: String?,
        isShared: Boolean,
        date: LocalDate,
    ): Result<Transaction> {
        lastCreatedClientId = clientId
        return createResult ?: Result.Error(NotImplementedError())
    }

    override suspend fun updateTransaction(
        id: String,
        amount: BigDecimal?,
        categoryId: String?,
        description: String?,
        isShared: Boolean?,
        date: LocalDate?,
    ): Result<Transaction> = updateResult ?: Result.Error(NotImplementedError())

    override suspend fun deleteTransaction(id: String): Result<Unit> = deleteResult
}
