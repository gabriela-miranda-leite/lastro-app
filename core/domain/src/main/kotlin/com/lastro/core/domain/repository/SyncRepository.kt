package com.lastro.core.domain.repository

import com.lastro.core.common.Result

interface SyncRepository {
    suspend fun syncPendingTransactions(): Result<Unit>
}
