package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor() : TransactionRepository {

    override fun getTransactions(): Flow<Result<List<Transaction>>> = flow {
        emit(Result.Success(emptyList()))
    }

    override suspend fun getTransaction(id: String): Result<Transaction> =
        Result.Error(NotImplementedError())

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> =
        Result.Success(Unit)

    override suspend fun deleteTransaction(id: String): Result<Unit> =
        Result.Success(Unit)
}
