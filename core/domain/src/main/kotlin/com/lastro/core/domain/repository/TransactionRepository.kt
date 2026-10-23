package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

interface TransactionRepository {
    suspend fun getTransactions(year: Int, month: Int, direction: TransactionType? = null, partnerUserId: String? = null): Result<List<Transaction>>
    suspend fun getTransaction(id: String): Result<Transaction>
    suspend fun createTransaction(
        clientId: String,
        amount: BigDecimal,
        direction: TransactionType,
        categoryId: String?,
        description: String?,
        isShared: Boolean,
        date: LocalDate,
    ): Result<Transaction>
    suspend fun updateTransaction(
        id: String,
        amount: BigDecimal?,
        categoryId: String?,
        description: String?,
        isShared: Boolean?,
        date: LocalDate?,
    ): Result<Transaction>
    suspend fun deleteTransaction(id: String): Result<Unit>
}
