package com.lastro.core.network.service

import com.lastro.core.network.dto.auth.AuthResponse
import com.lastro.core.network.dto.auth.ForgotPasswordRequest
import com.lastro.core.network.dto.auth.LoginRequest
import com.lastro.core.network.dto.auth.LogoutRequest
import com.lastro.core.network.dto.auth.MessageResponse
import com.lastro.core.network.dto.auth.RefreshRequest
import com.lastro.core.network.dto.auth.RegisterRequest
import com.lastro.core.network.dto.auth.ResetPasswordRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout(@Body body: LogoutRequest)

    @POST("auth/logout/all")
    suspend fun logoutAll()

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): MessageResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): MessageResponse
}
