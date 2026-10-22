package com.lastro.core.domain.di

import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.domain.repository.AuthRepositoryImpl
import com.lastro.core.domain.repository.CategoryRepository
import com.lastro.core.domain.repository.CategoryRepositoryImpl
import com.lastro.core.domain.repository.CoupleRepository
import com.lastro.core.domain.repository.CoupleRepositoryImpl
import com.lastro.core.domain.repository.TransactionRepository
import com.lastro.core.domain.repository.TransactionRepositoryImpl
import com.lastro.core.domain.repository.UserRepository
import com.lastro.core.domain.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindCoupleRepository(impl: CoupleRepositoryImpl): CoupleRepository
}
