package com.lastro.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.feature.auth.ui.LoginScreen
import com.lastro.feature.auth.ui.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(Route.Auth.Login.path) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Route.Dashboard.path) {
                    // Remove Login (e tudo abaixo) do back stack.
                    // inclusive = true: remove o próprio Login também.
                    // Resultado: apertar "voltar" no Dashboard fecha o app.
                    popUpTo(Route.Auth.Login.path) { inclusive = true }
                }
            },
            onNavigateToRegister = {
                navController.navigate(Route.Auth.Register.path)
            }
        )
    }

    composable(Route.Auth.Register.path) {
        RegisterScreen(
            onRegisterSuccess = {
                navController.navigate(Route.Dashboard.path) {
                    popUpTo(Route.Auth.Login.path) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }
}
