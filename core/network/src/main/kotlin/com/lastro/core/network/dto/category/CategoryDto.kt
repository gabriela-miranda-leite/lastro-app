package com.lastro.core.network.dto.category

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("icon") val icon: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val createdAt: String,
)
