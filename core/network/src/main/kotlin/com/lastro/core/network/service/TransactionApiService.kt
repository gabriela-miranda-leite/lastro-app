package com.lastro.core.network.service

import com.lastro.core.network.dto.transaction.CreateTransactionRequest
import com.lastro.core.network.dto.transaction.TransactionDto
import com.lastro.core.network.dto.transaction.UpdateTransactionRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApiService {
    @GET("transactions")
    suspend fun list(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("direction") direction: String? = null,
        @Query("category_id") categoryId: String? = null,
        @Query("user_id") userId: String? = null,
    ): List<TransactionDto>

    @POST("transactions")
    suspend fun create(@Body body: CreateTransactionRequest): TransactionDto

    @PUT("transactions/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateTransactionRequest): TransactionDto

    @DELETE("transactions/{id}")
    suspend fun delete(@Path("id") id: String)
}
