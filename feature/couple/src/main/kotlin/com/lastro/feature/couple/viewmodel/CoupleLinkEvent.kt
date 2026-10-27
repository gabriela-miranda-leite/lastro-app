package com.lastro.feature.couple.viewmodel

sealed class CoupleLinkEvent {
    data class ShareInvite(val code: String) : CoupleLinkEvent()
    data object LinkedSuccess : CoupleLinkEvent()
}
