package com.lastro.core.network.dto.couple

import com.google.gson.annotations.SerializedName

data class InviteResponse(
    @SerializedName("code") val code: String,
    @SerializedName("expires_at") val expiresAt: String,
)
