package com.lastro.core.network.auth

interface TokenStore {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getUserId(): String?
    fun saveTokens(accessToken: String, refreshToken: String, userId: String)
    fun clearTokens()
}
