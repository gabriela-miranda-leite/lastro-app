package com.lastro.core.network.di

import com.lastro.core.network.BuildConfig
import com.lastro.core.network.auth.AuthInterceptor
import com.lastro.core.network.auth.TokenStore
import com.lastro.core.network.auth.TokenStoreImpl
import com.lastro.core.network.service.AuthApiService
import com.lastro.core.network.service.CoupleApiService
import com.lastro.core.network.service.TransactionApiService
import com.lastro.core.network.service.UserApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindTokenStore(impl: TokenStoreImpl): TokenStore

    companion object {

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            authInterceptor: AuthInterceptor,
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
            retrofit.create(AuthApiService::class.java)

        @Provides
        @Singleton
        fun provideTransactionApiService(retrofit: Retrofit): TransactionApiService =
            retrofit.create(TransactionApiService::class.java)

        @Provides
        @Singleton
        fun provideUserApiService(retrofit: Retrofit): UserApiService =
            retrofit.create(UserApiService::class.java)

        @Provides
        @Singleton
        fun provideCoupleApiService(retrofit: Retrofit): CoupleApiService =
            retrofit.create(CoupleApiService::class.java)
    }
}
