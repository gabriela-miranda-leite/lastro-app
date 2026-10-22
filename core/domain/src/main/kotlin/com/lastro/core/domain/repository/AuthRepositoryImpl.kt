package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.common.mapSuccess
import com.lastro.core.network.auth.TokenStore
import com.lastro.core.network.dto.auth.LoginRequest
import com.lastro.core.network.dto.auth.LogoutRequest
import com.lastro.core.network.dto.auth.RefreshRequest
import com.lastro.core.network.dto.auth.RegisterRequest
import com.lastro.core.network.service.AuthApiService
import com.lastro.core.network.util.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val tokenStore: TokenStore,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = safeApiCall { api.login(LoginRequest(email, password)) }
        if (result is Result.Success) {
            tokenStore.saveTokens(result.data.accessToken, result.data.refreshToken)
        }
        return result.mapSuccess { Unit }
    }

    override suspend fun register(email: String, password: String, name: String): Result<Unit> {
        val result = safeApiCall { api.register(RegisterRequest(name, email, password)) }
        if (result is Result.Success) {
            tokenStore.saveTokens(result.data.accessToken, result.data.refreshToken)
        }
        return result.mapSuccess { Unit }
    }

    override suspend fun logout() {
        val refreshToken = tokenStore.getRefreshToken() ?: return
        safeApiCall { api.logout(LogoutRequest(refreshToken)) }
        tokenStore.clearTokens()
    }

    override suspend fun isLoggedIn(): Boolean = tokenStore.getAccessToken() != null

    suspend fun refresh(): Result<Unit> {
        val refreshToken = tokenStore.getRefreshToken()
            ?: return Result.Error(IllegalStateException("No refresh token"))
        val result = safeApiCall { api.refresh(RefreshRequest(refreshToken)) }
        if (result is Result.Success) {
            tokenStore.saveTokens(result.data.accessToken, result.data.refreshToken)
        }
        return result.mapSuccess { Unit }
    }
}
