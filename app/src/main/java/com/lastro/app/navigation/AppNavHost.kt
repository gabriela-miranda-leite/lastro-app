package com.lastro.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lastro.core.common.navigation.Route
import com.lastro.feature.auth.navigation.authGraph
import com.lastro.feature.couple.navigation.coupleGraph
import com.lastro.feature.dashboard.navigation.dashboardGraph
import com.lastro.feature.profile.navigation.profileGraph
import com.lastro.feature.transactions.navigation.transactionsGraph

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Auth.Login.path
    ) {
        authGraph(navController)
        dashboardGraph(navController)
        transactionsGraph(navController)
        coupleGraph(navController)
        profileGraph(navController)
    }
}
