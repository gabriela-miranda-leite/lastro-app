package com.lastro.core.testing.di

import com.lastro.core.domain.di.RepositoryModule
import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.testing.repository.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(fake: FakeAuthRepository): AuthRepository
}
