package com.lastro.feature.couple.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastro.core.common.Result
import com.lastro.core.domain.exception.CoupleException
import com.lastro.core.domain.usecase.GenerateCoupleInviteUseCase
import com.lastro.core.domain.usecase.LinkCoupleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoupleLinkViewModel @Inject constructor(
    private val generateInvite: GenerateCoupleInviteUseCase,
    private val linkCouple: LinkCoupleUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoupleLinkUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CoupleLinkEvent>()
    val events = _events.asSharedFlow()

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index, error = null)
    }

    fun onPartnerEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(partnerEmail = email)
    }

    fun onInviteCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(inviteCode = code, error = null)
    }

    fun sendInvite() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = generateInvite()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        generatedInvite = result.data,
                    )
                    _events.emit(CoupleLinkEvent.ShareInvite(result.data.code))
                }
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when (result.exception) {
                        is CoupleException.AlreadyLinked -> "Você já está vinculado a um casal."
                        else -> "Erro ao gerar convite. Tente novamente."
                    },
                )
                is Result.Loading -> Unit
            }
        }
    }

    fun joinCouple() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = linkCouple(_uiState.value.inviteCode.trim())) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isLinked = true)
                    _events.emit(CoupleLinkEvent.LinkedSuccess)
                }
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = when (result.exception) {
                        is CoupleException.InviteExpired -> "Este código expirou ou já foi utilizado."
                        is CoupleException.InviteNotFound -> "Código não encontrado. Verifique e tente novamente."
                        is CoupleException.AlreadyLinked -> "Você já está vinculado a um casal."
                        is IllegalArgumentException -> "O código não pode estar vazio."
                        else -> "Erro ao entrar no casal. Tente novamente."
                    },
                )
                is Result.Loading -> Unit
            }
        }
    }
}
