package com.lastro.feature.auth.viewmodel

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
