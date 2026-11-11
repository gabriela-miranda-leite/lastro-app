package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val invalidToken: Boolean = false,
)

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun submit(token: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState(isLoading = true)
            when (authRepository.resetPassword(token, newPassword)) {
                is Result.Success -> _uiState.value = ResetPasswordUiState(success = true)
                is Result.Error -> _uiState.value = ResetPasswordUiState(invalidToken = true)
                is Result.Loading -> Unit
            }
        }
    }
}
