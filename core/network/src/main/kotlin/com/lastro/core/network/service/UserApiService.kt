package com.lastro.core.network.service

import com.lastro.core.network.dto.user.ChangePasswordRequest
import com.lastro.core.network.dto.user.DeleteAccountRequest
import com.lastro.core.network.dto.user.UpdateUserRequest
import com.lastro.core.network.dto.user.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApiService {
    @GET("users/me")
    suspend fun getMe(): UserDto

    @PUT("users/me")
    suspend fun updateMe(@Body body: UpdateUserRequest): UserDto

    @PUT("users/me/password")
    suspend fun changePassword(@Body body: ChangePasswordRequest)

    @DELETE("users/me")
    suspend fun deleteAccount(@Body body: DeleteAccountRequest)

    @GET("users/me/export")
    suspend fun export(): String
}
