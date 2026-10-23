package com.lastro.core.network.util

import com.lastro.core.common.Result
import com.lastro.core.network.exception.ApiException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SafeApiCallTest {

    @Test
    fun `returns Success when call succeeds`() = runTest {
        val result = safeApiCall { "ok" }
        assertEquals(Result.Success("ok"), result)
    }

    @Test
    fun `maps 401 to Unauthorized`() = runTest {
        val result = safeApiCall<String> { throw httpException(401) }
        assertEquals(Result.Error(ApiException.Unauthorized), result)
    }

    @Test
    fun `maps 404 to NotFound`() = runTest {
        val result = safeApiCall<String> { throw httpException(404) }
        assertEquals(Result.Error(ApiException.NotFound), result)
    }

    @Test
    fun `maps other HTTP codes to ServerError`() = runTest {
        val result = safeApiCall<String> { throw httpException(500) }
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ApiException.ServerError)
        assertEquals(500, (result.exception as ApiException.ServerError).code)
    }

    @Test
    fun `maps IOException to NetworkError`() = runTest {
        val cause = IOException("timeout")
        val result = safeApiCall<String> { throw cause }
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is ApiException.NetworkError)
    }

    private fun httpException(code: Int) =
        HttpException(Response.error<Any>(code, "".toResponseBody()))
}
