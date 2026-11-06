package com.lastro.core.network.dto.recurring

import com.google.gson.annotations.SerializedName

data class UpdateRecurringRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("amount") val amount: String?,
    @SerializedName("due_day") val dueDay: Int?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("active") val active: Boolean?,
)
