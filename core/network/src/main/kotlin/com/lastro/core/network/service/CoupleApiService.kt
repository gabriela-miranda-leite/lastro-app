package com.lastro.core.network.service

import com.lastro.core.network.dto.couple.InviteResponse
import com.lastro.core.network.dto.couple.JoinRequest
import com.lastro.core.network.dto.couple.JoinResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface CoupleApiService {
    @POST("couples/invite")
    suspend fun invite(): InviteResponse

    @POST("couples/join")
    suspend fun join(@Body body: JoinRequest): JoinResponse

    @DELETE("couples")
    suspend fun unlink()
}
