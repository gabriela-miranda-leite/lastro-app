package com.lastro.core.network.service

import com.lastro.core.network.dto.dashboard.DashboardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DashboardApiService {
    @GET("dashboard/{year}/{month}")
    suspend fun get(@Path("year") year: Int, @Path("month") month: Int): DashboardResponse
}
