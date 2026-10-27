package com.lastro.feature.couple.viewmodel

import com.lastro.core.domain.model.CoupleInvite

data class CoupleLinkUiState(
    val selectedTab: Int = 0,
    val partnerEmail: String = "",
    val inviteCode: String = "",
    val generatedInvite: CoupleInvite? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLinked: Boolean = false,
)
