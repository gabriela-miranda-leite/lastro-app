package com.lastro.core.network.dto.goal

import com.google.gson.annotations.SerializedName

data class GoalDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("goal_amount") val goalAmount: String,
    @SerializedName("saved_amount") val savedAmount: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
)

data class GoalWithStatusDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("goal_amount") val goalAmount: String,
    @SerializedName("saved_amount") val savedAmount: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("status") val status: String,
)
