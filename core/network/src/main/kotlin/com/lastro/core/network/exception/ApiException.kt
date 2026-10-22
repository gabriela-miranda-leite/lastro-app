package com.lastro.core.network.exception

sealed class ApiException(message: String) : Exception(message) {
    data object Unauthorized : ApiException("Unauthorized")
    data object NotFound : ApiException("Not found")
    data class ServerError(val code: Int) : ApiException("Server error: $code")
    data class NetworkError(val cause: Throwable) : ApiException("Network error: ${cause.message}")
}
