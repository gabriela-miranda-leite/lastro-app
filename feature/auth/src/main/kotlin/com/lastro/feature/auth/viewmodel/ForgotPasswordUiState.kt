package com.lastro.feature.auth.viewmodel

import com.lastro.core.ui.UiText

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val success: Boolean = false,
)
