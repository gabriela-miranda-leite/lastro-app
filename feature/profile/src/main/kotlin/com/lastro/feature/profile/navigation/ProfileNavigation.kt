package com.lastro.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.core.common.navigation.deepLinksFor
import com.lastro.feature.profile.ui.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(Route.Profile.path, deepLinks = deepLinksFor(Route.Profile.path)) {
        ProfileScreen(
            onLogout = {
                navController.navigate(Route.Auth.Login.path) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }
}
