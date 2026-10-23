package com.lastro.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val clientId: String,
    val userId: String,
    val categoryId: String?,
    val amount: String,
    val direction: String,
    val description: String?,
    val isShared: Boolean,
    val date: String,
    val updatedAt: String,
    val syncStatus: String,
)
