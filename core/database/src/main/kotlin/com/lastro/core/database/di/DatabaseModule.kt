package com.lastro.core.database.di

import android.content.Context
import androidx.room.Room
import com.lastro.core.database.LastroDatabase
import com.lastro.core.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLastroDatabase(@ApplicationContext context: Context): LastroDatabase =
        Room.databaseBuilder(
            context,
            LastroDatabase::class.java,
            "lastro.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideTransactionDao(db: LastroDatabase): TransactionDao = db.transactionDao()
}
