package com.lastro.core.network.exception

sealed class ApiException(message: String) : Exception(message) {
    data object Unauthorized : ApiException("Unauthorized")
    data object NotFound : ApiException("Not found")
    data object Conflict : ApiException("Conflict")
    data object Gone : ApiException("Gone")
    data object TooManyRequests : ApiException("Too many requests")
    data class ServerError(val code: Int) : ApiException("Server error: $code")
    data class NetworkError(override val cause: Throwable) : ApiException("Network error: ${cause.message}")
}
