package com.lastro.core.network.service

import com.lastro.core.network.dto.category.CategoryDto
import com.lastro.core.network.dto.category.CreateCategoryRequest
import com.lastro.core.network.dto.category.UpdateCategoryRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApiService {
    @GET("categories")
    suspend fun list(): List<CategoryDto>

    @POST("categories")
    suspend fun create(@Body body: CreateCategoryRequest): CategoryDto

    @PUT("categories/{id}")
    suspend fun update(@Path("id") id: String, @Body body: UpdateCategoryRequest): CategoryDto

    @DELETE("categories/{id}")
    suspend fun delete(@Path("id") id: String)
}
