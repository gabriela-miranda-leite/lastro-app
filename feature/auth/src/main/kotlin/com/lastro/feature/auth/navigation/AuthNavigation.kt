package com.lastro.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.feature.auth.ui.ForgotPasswordScreen
import com.lastro.feature.auth.ui.LoginScreen
import com.lastro.feature.auth.ui.RegisterScreen
import com.lastro.feature.auth.ui.SplashScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(Route.Splash.path) {
        SplashScreen(
            onNavigateToHome = {
                navController.navigate(Route.Dashboard.path) {
                    popUpTo(Route.Splash.path) { inclusive = true }
                }
            },
            onNavigateToLogin = {
                navController.navigate(Route.Auth.Login.path) {
                    popUpTo(Route.Splash.path) { inclusive = true }
                }
            },
        )
    }

    composable(Route.Auth.Login.path) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Route.Dashboard.path) {
                    popUpTo(Route.Auth.Login.path) { inclusive = true }
                }
            },
            onNavigateToRegister = {
                navController.navigate(Route.Auth.Register.path)
            },
            onNavigateToForgotPassword = {
                navController.navigate(Route.Auth.ForgotPassword.path)
            },
        )
    }

    composable(Route.Auth.Register.path) {
        RegisterScreen(
            onRegisterSuccess = {
                navController.navigate(Route.Dashboard.path) {
                    popUpTo(Route.Auth.Login.path) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() },
        )
    }

    composable(Route.Auth.ForgotPassword.path) {
        ForgotPasswordScreen(
            onBack = { navController.popBackStack() },
        )
    }
}
