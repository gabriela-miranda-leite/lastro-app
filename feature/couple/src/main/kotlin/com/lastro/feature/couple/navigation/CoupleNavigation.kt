package com.lastro.feature.couple.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.feature.couple.ui.CoupleLinkScreen

fun NavGraphBuilder.coupleGraph(navController: NavController) {
    composable(Route.Couple.Home.path) {
        CoupleLinkScreen(
            onBack = { navController.popBackStack() },
            onLinked = { navController.navigate(Route.Dashboard.path) {
                popUpTo(Route.Couple.Home.path) { inclusive = true }
            }},
        )
    }
}
