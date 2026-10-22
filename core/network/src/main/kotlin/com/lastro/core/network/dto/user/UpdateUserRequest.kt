package com.lastro.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("monthly_income") val monthlyIncome: String?,
    @SerializedName("saving_goal_pct") val savingGoalPct: Double?,
)
