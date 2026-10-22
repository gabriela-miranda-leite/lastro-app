package com.lastro.core.network.dto.transaction

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("id") val id: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("amount") val amount: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("description") val description: String?,
    @SerializedName("is_shared") val isShared: Boolean,
    @SerializedName("date") val date: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
)
