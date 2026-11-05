package com.lastro.core.network.service

import com.lastro.core.network.dto.goal.CreateGoalRequest
import com.lastro.core.network.dto.goal.GoalDto
import com.lastro.core.network.dto.goal.GoalWithStatusDto
import com.lastro.core.network.dto.goal.UpdateGoalRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GoalApiService {
    @GET("goals")
    suspend fun list(
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null,
    ): List<GoalWithStatusDto>

    @POST("goals")
    suspend fun create(@Body body: CreateGoalRequest): GoalDto

    @PUT("goals/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateGoalRequest): GoalDto
}
