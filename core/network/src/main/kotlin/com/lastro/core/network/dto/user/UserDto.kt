package com.lastro.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("monthly_income") val monthlyIncome: String?,
    @SerializedName("saving_goal_pct") val savingGoalPct: Double?,
    @SerializedName("couple_id") val coupleId: String?,
    @SerializedName("partner") val partner: PartnerDto?,
)

data class PartnerDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
)
