package com.lastro.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DashboardEvent>()
    val events = _events.asSharedFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getUserProfile()) {
                is Result.Success -> _uiState.update {
                    it.copy(isLoading = false, userName = result.data.name.firstName())
                }
                is Result.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
                is Result.Loading -> Unit
            }
        }
    }

    fun toggleBalanceVisibility() {
        _uiState.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
    }

    fun onNewTransaction() {
        viewModelScope.launch { _events.emit(DashboardEvent.NavigateToNewTransaction) }
    }

    fun onAllTransactions() {
        viewModelScope.launch { _events.emit(DashboardEvent.NavigateToAllTransactions) }
    }

    private fun String.firstName(): String = trim().split(" ").firstOrNull() ?: this
}
