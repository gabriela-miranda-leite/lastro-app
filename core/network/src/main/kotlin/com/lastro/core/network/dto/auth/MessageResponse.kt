package com.lastro.core.network.dto.auth

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String,
)
