package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.network.exception.ApiException
import com.lastro.core.ui.UiText
import com.lastro.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> _events.emit(AuthEvent.LoginSuccess)
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when (result.exception) {
                        is ApiException.Unauthorized -> UiText.Res(R.string.login_error_unauthorized)
                        is ApiException.TooManyRequests -> UiText.Res(R.string.forgot_password_error_too_many)
                        else -> result.message?.let { UiText.Literal(it) }
                            ?: UiText.Res(R.string.login_error_generic)
                    },
                )
                is Result.Loading -> Unit
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.register(email, password, name)) {
                is Result.Success -> _events.emit(AuthEvent.RegisterSuccess)
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message?.let { UiText.Literal(it) }
                        ?: UiText.Res(R.string.register_error_generic),
                )
                is Result.Loading -> Unit
            }
        }
    }
}
