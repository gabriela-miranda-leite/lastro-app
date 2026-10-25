package com.lastro.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class OnboardingIncomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface OnboardingIncomeEvent {
    data object Success : OnboardingIncomeEvent
}

@HiltViewModel
class OnboardingIncomeViewModel @Inject constructor(
    private val updateUserProfile: UpdateUserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingIncomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingIncomeEvent>()
    val events = _events.asSharedFlow()

    fun submit(income: BigDecimal) {
        viewModelScope.launch {
            _uiState.value = OnboardingIncomeUiState(isLoading = true)
            when (val result = updateUserProfile(monthlyIncome = income, name = null, savingGoalPct = null)) {
                is Result.Success -> _events.emit(OnboardingIncomeEvent.Success)
                is Result.Error -> _uiState.value = OnboardingIncomeUiState(
                    error = "Não foi possível salvar a renda. Tente novamente"
                )
                is Result.Loading -> Unit
            }
        }
    }
}
