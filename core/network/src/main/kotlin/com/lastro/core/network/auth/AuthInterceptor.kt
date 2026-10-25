package com.lastro.core.network.auth

import com.lastro.core.network.dto.auth.RefreshRequest
import com.lastro.core.network.service.AuthApiService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
    private val authApi: Lazy<AuthApiService>,
) : Interceptor {

    private val refreshLock = Any()

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStore.getAccessToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        if (response.code == 401 && token != null) {
            response.close()
            val newToken = synchronized(refreshLock) {
                val currentToken = tokenStore.getAccessToken()
                if (currentToken != null && currentToken != token) {
                    currentToken
                } else {
                    val refreshToken = tokenStore.getRefreshToken() ?: return@synchronized null
                    val refreshed = runBlocking {
                        try {
                            authApi.get().refresh(RefreshRequest(refreshToken))
                        } catch (_: Exception) {
                            null
                        }
                    }
                    if (refreshed != null) {
                        tokenStore.saveTokens(refreshed.accessToken, refreshed.refreshToken, refreshed.user.id)
                        refreshed.accessToken
                    } else {
                        tokenStore.clearTokens()
                        null
                    }
                }
            }

            return if (newToken != null) {
                chain.proceed(
                    request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                )
            } else {
                chain.proceed(request)
            }
        }

        return response
    }
}
