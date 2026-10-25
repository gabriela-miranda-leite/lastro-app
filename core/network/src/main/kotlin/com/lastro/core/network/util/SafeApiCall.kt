package com.lastro.core.network.util

import com.lastro.core.common.Result
import com.lastro.core.network.exception.ApiException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> = try {
    Result.Success(call())
} catch (e: HttpException) {
    when (e.code()) {
        401 -> Result.Error(ApiException.Unauthorized)
        404 -> Result.Error(ApiException.NotFound)
        429 -> Result.Error(ApiException.TooManyRequests)
        else -> Result.Error(ApiException.ServerError(e.code()))
    }
} catch (e: IOException) {
    Result.Error(ApiException.NetworkError(e))
}
