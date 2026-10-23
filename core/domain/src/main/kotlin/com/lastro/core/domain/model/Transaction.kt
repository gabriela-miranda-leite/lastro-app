package com.lastro.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: String,
    val clientId: String,
    val userId: String,
    val amount: BigDecimal,
    val direction: TransactionType,
    val category: Category?,
    val description: String?,
    val isShared: Boolean,
    val date: LocalDate,
    val syncStatus: SyncStatus,
)
