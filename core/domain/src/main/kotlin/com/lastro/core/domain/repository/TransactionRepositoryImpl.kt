package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.common.mapSuccess
import com.lastro.core.database.dao.TransactionDao
import com.lastro.core.database.entity.TransactionEntity
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.network.dto.transaction.CreateTransactionRequest
import com.lastro.core.network.dto.transaction.TransactionDto
import com.lastro.core.network.dto.transaction.UpdateTransactionRequest
import com.lastro.core.network.service.TransactionApiService
import com.lastro.core.domain.worker.SyncWorker
import com.lastro.core.network.auth.TokenStore
import com.lastro.core.network.util.safeApiCall
import androidx.work.WorkManager
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApiService,
    private val dao: TransactionDao,
    private val workManager: WorkManager,
    private val tokenStore: TokenStore,
) : TransactionRepository {

    override suspend fun getTransactions(year: Int, month: Int, direction: TransactionType?): Result<List<Transaction>> {
        val apiResult = safeApiCall {
            api.list(year, month, direction?.name)
        }
        if (apiResult is Result.Success) {
            dao.upsertAll(apiResult.data.map { it.toEntity(SyncStatus.SYNCED) })
        }
        val yearMonth = "$year-${month.toString().padStart(2, '0')}"
        val cached = dao.getByMonth(yearMonth)
        return Result.Success(cached.map { it.toDomain() })
    }

    override suspend fun getTransaction(id: String): Result<Transaction> {
        val cached = dao.getById(id)
        if (cached != null) return Result.Success(cached.toDomain())
        return Result.Error(NoSuchElementException("Transaction not found"))
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
        val pending = TransactionEntity(
            id = clientId,
            clientId = clientId,
            userId = tokenStore.getUserId().orEmpty(),
            categoryId = categoryId,
            amount = amount.toPlainString(),
            direction = direction.name,
            description = description,
            isShared = isShared,
            date = date.toString(),
            updatedAt = "",
            syncStatus = SyncStatus.PENDING.name,
        )
        dao.upsert(pending)

        val result = safeApiCall {
            api.create(
                CreateTransactionRequest(
                    clientId = clientId,
                    amount = amount.toPlainString(),
                    direction = direction.name,
                    categoryId = categoryId,
                    description = description,
                    isShared = isShared,
                    date = date.toString(),
                )
            )
        }
        return when (result) {
            is Result.Success -> {
                val synced = result.data.toEntity(SyncStatus.SYNCED)
                dao.upsert(synced)
                dao.deleteById(clientId)
                Result.Success(synced.toDomain())
            }
            is Result.Error -> {
                dao.upsert(pending.copy(syncStatus = SyncStatus.ERROR.name))
                SyncWorker.scheduleOneTime(workManager)
                result
            }
            else -> result as Result<Transaction>
        }
    }

    override suspend fun updateTransaction(
        id: String,
        amount: BigDecimal?,
        categoryId: String?,
        description: String?,
        isShared: Boolean?,
        date: LocalDate?,
    ): Result<Transaction> {
        val result = safeApiCall {
            api.update(
                id,
                UpdateTransactionRequest(
                    amount = amount?.toPlainString(),
                    categoryId = categoryId,
                    description = description,
                    isShared = isShared,
                    date = date?.toString(),
                )
            )
        }
        if (result is Result.Success) {
            dao.upsert(result.data.toEntity(SyncStatus.SYNCED))
        } else if (result is Result.Error) {
            SyncWorker.scheduleOneTime(workManager)
        }
        return result.mapSuccess { it.toEntity(SyncStatus.SYNCED).toDomain() }
    }

    override suspend fun deleteTransaction(id: String): Result<Unit> {
        val result = safeApiCall { api.delete(id) }
        if (result is Result.Success) {
            dao.deleteById(id)
        }
        return result.mapSuccess { Unit }
    }

    private fun TransactionDto.toEntity(status: SyncStatus) = TransactionEntity(
        id = id,
        clientId = clientId,
        userId = userId,
        categoryId = categoryId,
        amount = amount,
        direction = direction,
        description = description,
        isShared = isShared,
        date = date,
        updatedAt = updatedAt,
        syncStatus = status.name,
    )

    private fun TransactionEntity.toDomain() = Transaction(
        id = id,
        clientId = clientId,
        amount = BigDecimal(amount),
        direction = TransactionType.valueOf(direction),
        category = null,
        description = description,
        isShared = isShared,
        date = LocalDate.parse(date),
        syncStatus = SyncStatus.valueOf(syncStatus),
    )
}
