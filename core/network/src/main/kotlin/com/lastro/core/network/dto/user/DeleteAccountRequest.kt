package com.lastro.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class DeleteAccountRequest(
    @SerializedName("password") val password: String,
)
