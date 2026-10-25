package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import com.lastro.core.network.exception.ApiException
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
                        ApiException.TooManyRequests.message -> "Muitas tentativas. Aguarde alguns minutos"
                        else -> "Não foi possível enviar o link. Tente novamente"
                    }
                )
                is Result.Loading -> Unit
            }
        }
    }
}
