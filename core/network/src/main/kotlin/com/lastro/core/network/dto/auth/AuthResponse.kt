package com.lastro.core.network.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user") val user: AuthUserDto,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
)

data class AuthUserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
)
