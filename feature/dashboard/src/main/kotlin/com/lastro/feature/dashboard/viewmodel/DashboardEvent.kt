package com.lastro.feature.dashboard.viewmodel

sealed interface DashboardEvent {
    data object NavigateToNewTransaction : DashboardEvent
    data object NavigateToAllTransactions : DashboardEvent
}
