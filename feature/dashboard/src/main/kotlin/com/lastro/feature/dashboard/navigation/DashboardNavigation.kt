package com.lastro.feature.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.core.common.navigation.deepLinksFor
import com.lastro.feature.dashboard.ui.DashboardScreen

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable(Route.Dashboard.path, deepLinks = deepLinksFor(Route.Dashboard.path)) {
        DashboardScreen(
            onNavigateToTransactions = {
                navController.navigate(Route.Transactions.List.path)
            },
            onNavigateToProfile = {
                navController.navigate(Route.Profile.path)
            },
            onNavigateToNewTransaction = {
                navController.navigate(Route.Transactions.Form().path)
            },
        )
    }
}
