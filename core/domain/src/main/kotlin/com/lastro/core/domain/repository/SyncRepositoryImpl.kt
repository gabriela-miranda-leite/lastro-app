package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.database.dao.TransactionDao
import com.lastro.core.database.entity.TransactionEntity
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.network.dto.transaction.CreateTransactionRequest
import com.lastro.core.network.service.TransactionApiService
import com.lastro.core.network.util.safeApiCall
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val api: TransactionApiService,
    private val dao: TransactionDao,
) : SyncRepository {

    override suspend fun syncPendingTransactions(): Result<Unit> {
        val pending = dao.getPendingOrError()
        var hasError = false

        for (entity in pending) {
            val result = safeApiCall {
                api.create(entity.toRequest())
            }
            when (result) {
                is Result.Success -> {
                    val synced = result.data.run {
                        TransactionEntity(
                            id = id,
                            clientId = clientId,
                            userId = userId,
                            categoryId = categoryId,
                            amount = amount,
                            direction = direction,
                            description = description,
                            isShared = isShared,
                            date = date,
                            syncStatus = SyncStatus.SYNCED.name,
                        )
                    }
                    dao.upsert(synced)
                    if (synced.id != entity.id) dao.deleteById(entity.id)
                }
                is Result.Error -> {
                    dao.upsert(entity.copy(syncStatus = SyncStatus.ERROR.name))
                    hasError = true
                }
                else -> Unit
            }
        }

        return if (hasError) Result.Error(Exception("One or more transactions failed to sync"))
        else Result.Success(Unit)
    }

    private fun TransactionEntity.toRequest() = CreateTransactionRequest(
        clientId = clientId,
        amount = amount,
        direction = direction,
        categoryId = categoryId,
        description = description,
        isShared = isShared,
        date = date,
    )
}
