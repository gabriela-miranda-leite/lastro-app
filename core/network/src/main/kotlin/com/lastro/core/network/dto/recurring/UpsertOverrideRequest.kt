package com.lastro.core.network.dto.recurring

import com.google.gson.annotations.SerializedName

data class UpsertOverrideRequest(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("amount_override") val amountOverride: String?,
    @SerializedName("active") val active: Boolean,
)
