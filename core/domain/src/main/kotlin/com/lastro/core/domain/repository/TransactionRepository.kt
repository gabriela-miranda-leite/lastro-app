package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<Result<List<Transaction>>>
    suspend fun getTransaction(id: String): Result<Transaction>
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(id: String): Result<Unit>
}
