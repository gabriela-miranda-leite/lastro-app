package com.lastro.core.network.dto.transaction

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("client_id") val clientId: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_shared") val isShared: Boolean,
    @SerializedName("date") val date: String,
)
