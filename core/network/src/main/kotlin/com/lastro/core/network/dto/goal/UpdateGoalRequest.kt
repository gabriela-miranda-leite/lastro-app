package com.lastro.core.network.dto.goal

import com.google.gson.annotations.SerializedName

data class UpdateGoalRequest(
    @SerializedName("saved_amount") val savedAmount: String?,
)
