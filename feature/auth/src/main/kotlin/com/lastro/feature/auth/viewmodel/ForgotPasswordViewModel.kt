package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.UiText
import com.lastro.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun submit(email: String) {
        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState(isLoading = true)
            when (val result = authRepository.forgotPassword(email)) {
                is Result.Success -> _uiState.value = ForgotPasswordUiState(success = true)
                is Result.Error -> _uiState.value = ForgotPasswordUiState(
                    error = when (result.message) {
                        ApiException.TooManyRequests.message ->
                            UiText.Res(R.string.forgot_password_error_too_many)
                        else ->
                            UiText.Res(R.string.forgot_password_error_generic)
                    }
                )
                is Result.Loading -> Unit
            }
        }
    }
}
