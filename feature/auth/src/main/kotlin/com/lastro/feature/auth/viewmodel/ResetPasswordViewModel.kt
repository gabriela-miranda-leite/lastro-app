package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface ResetPasswordEvent {
    data object Success : ResetPasswordEvent
}

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ResetPasswordEvent>()
    val events = _events.asSharedFlow()

    fun submit(token: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState(isLoading = true)
            when (val result = authRepository.resetPassword(token, newPassword)) {
                is Result.Success -> _events.emit(ResetPasswordEvent.Success)
                is Result.Error -> _uiState.value = ResetPasswordUiState(
                    error = "Link inválido ou expirado. Solicite um novo link."
                )
                is Result.Loading -> Unit
            }
        }
    }
}
