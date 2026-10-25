package com.lastro.feature.auth.viewmodel

import com.lastro.core.ui.UiText

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
)
