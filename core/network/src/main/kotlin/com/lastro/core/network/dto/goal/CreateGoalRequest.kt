package com.lastro.core.network.dto.goal

import com.google.gson.annotations.SerializedName

data class CreateGoalRequest(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("goal_amount") val goalAmount: String,
)
