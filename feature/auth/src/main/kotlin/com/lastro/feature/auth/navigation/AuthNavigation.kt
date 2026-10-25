package com.lastro.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lastro.core.common.navigation.Route
import com.lastro.feature.auth.ui.ForgotPasswordScreen
import com.lastro.feature.auth.ui.LoginScreen
import com.lastro.feature.auth.ui.OnboardingIncomeScreen
import com.lastro.feature.auth.ui.RegisterScreen
import com.lastro.feature.auth.ui.ResetPasswordScreen
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
                navController.navigate(Route.Auth.OnboardingIncome.path) {
                    popUpTo(Route.Auth.Register.path) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() },
        )
    }

    composable(Route.Auth.OnboardingIncome.path) {
        OnboardingIncomeScreen(
            onSuccess = {
                navController.navigate(Route.Dashboard.path) {
                    popUpTo(Route.Auth.Login.path) { inclusive = true }
                }
            },
        )
    }

    composable(Route.Auth.ForgotPassword.path) {
        ForgotPasswordScreen(
            onBack = { navController.popBackStack() },
        )
    }

    composable(
        route = "auth/reset-password/{token}",
        arguments = listOf(navArgument("token") { type = NavType.StringType }),
    ) { backStackEntry ->
        val token = backStackEntry.arguments?.getString("token").orEmpty()
        ResetPasswordScreen(
            token = token,
            onSuccess = {
                navController.navigate(Route.Auth.Login.path) {
                    popUpTo("auth/reset-password/{token}") { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() },
        )
    }
}
