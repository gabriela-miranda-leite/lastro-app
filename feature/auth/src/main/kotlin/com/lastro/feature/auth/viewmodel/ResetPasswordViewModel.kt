package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.ui.UiText
import com.lastro.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
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
            when (authRepository.resetPassword(token, newPassword)) {
                is Result.Success -> _events.emit(ResetPasswordEvent.Success)
                is Result.Error -> _uiState.value = ResetPasswordUiState(
                    error = UiText.Res(R.string.reset_password_error_link)
                )
                is Result.Loading -> Unit
            }
        }
    }
}
