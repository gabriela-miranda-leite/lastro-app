package com.lastro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lastro.core.database.dao.TransactionDao
import com.lastro.core.database.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class LastroDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
