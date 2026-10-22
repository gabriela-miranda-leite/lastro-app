package com.lastro.core.network.dto.transaction

import com.google.gson.annotations.SerializedName

data class UpdateTransactionRequest(
    @SerializedName("amount") val amount: String?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_shared") val isShared: Boolean?,
    @SerializedName("date") val date: String?,
)
