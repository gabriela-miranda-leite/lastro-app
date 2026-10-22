package com.lastro.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lastro.core.database.entity.TransactionEntity

@Dao
@JvmSuppressWildcards
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE date LIKE :yearMonth || '%' ORDER BY date DESC")
    suspend fun getByMonth(yearMonth: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TransactionEntity>): List<Long>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
