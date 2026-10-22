package com.lastro.core.common

// Sealed class base para eventos de navegação que saem do ViewModel.
// Cada feature define seus próprios eventos herdando desta.
// O Composable escuta via LaunchedEffect e chama navController.navigate().
sealed interface UiEvent
