package com.lastro.core.network.dto.couple

import com.google.gson.annotations.SerializedName

data class JoinResponse(
    @SerializedName("couple_id") val coupleId: String,
)
