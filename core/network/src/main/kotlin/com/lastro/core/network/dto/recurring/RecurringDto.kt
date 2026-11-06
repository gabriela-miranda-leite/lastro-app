package com.lastro.core.network.dto.recurring

import com.google.gson.annotations.SerializedName

data class RecurringDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("due_day") val dueDay: Int,
    @SerializedName("active") val active: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
)

data class RecurringOverrideDto(
    @SerializedName("id") val id: String,
    @SerializedName("recurring_id") val recurringId: String,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("amount_override") val amountOverride: String?,
    @SerializedName("active") val active: Boolean,
    @SerializedName("created_at") val createdAt: String,
)
