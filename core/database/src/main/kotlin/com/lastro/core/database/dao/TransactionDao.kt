package com.lastro.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lastro.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun observeAll(userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND date LIKE :yearMonth || '%' ORDER BY date DESC")
    fun observeByMonth(yearMonth: String, userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(CAST(amount AS REAL)) FROM transactions WHERE userId = :userId AND direction = 'INCOME'")
    fun observeTotalIncome(userId: String): Flow<Double?>

    @Query("SELECT SUM(CAST(amount AS REAL)) FROM transactions WHERE userId = :userId AND direction = 'EXPENSE'")
    fun observeTotalExpense(userId: String): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE date LIKE :yearMonth || '%' ORDER BY date DESC")
    suspend fun getByMonth(yearMonth: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TransactionEntity>): List<Long>

    @Query("SELECT * FROM transactions WHERE syncStatus IN ('PENDING', 'ERROR')")
    suspend fun getPendingOrError(): List<TransactionEntity>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
