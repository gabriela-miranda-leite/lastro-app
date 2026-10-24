package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashEvent {
    data object NavigateToHome : SplashEvent
    data object NavigateToLogin : SplashEvent
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _events = MutableSharedFlow<SplashEvent>()
    val events = _events.asSharedFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            if (!authRepository.isLoggedIn()) {
                _events.emit(SplashEvent.NavigateToLogin)
                return@launch
            }
            val refreshResult = authRepository.refresh()
            if (refreshResult is Result.Success) {
                _events.emit(SplashEvent.NavigateToHome)
            } else {
                _events.emit(SplashEvent.NavigateToLogin)
            }
        }
    }
}
