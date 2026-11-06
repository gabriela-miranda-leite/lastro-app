package com.lastro.core.network.service

import com.lastro.core.network.dto.recurring.CreateRecurringRequest
import com.lastro.core.network.dto.recurring.RecurringDto
import com.lastro.core.network.dto.recurring.RecurringOverrideDto
import com.lastro.core.network.dto.recurring.UpdateRecurringRequest
import com.lastro.core.network.dto.recurring.UpsertOverrideRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecurringApiService {
    @GET("recurring")
    suspend fun list(): List<RecurringDto>

    @POST("recurring")
    suspend fun create(@Body body: CreateRecurringRequest): RecurringDto

    @PUT("recurring/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateRecurringRequest): RecurringDto

    @DELETE("recurring/{id}")
    suspend fun delete(@Path("id") id: String)

    @PUT("recurring/{id}/override")
    suspend fun upsertOverride(@Path("id") id: String, @Body body: UpsertOverrideRequest): RecurringOverrideDto
}
