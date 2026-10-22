package com.lastro.core.network.dto.couple

import com.google.gson.annotations.SerializedName

data class JoinRequest(
    @SerializedName("code") val code: String,
)
