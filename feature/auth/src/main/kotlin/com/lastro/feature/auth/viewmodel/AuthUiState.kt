package com.lastro.feature.auth.viewmodel

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)
