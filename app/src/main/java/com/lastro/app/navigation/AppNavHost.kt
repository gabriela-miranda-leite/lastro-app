package com.lastro.app.navigation

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.util.Consumer
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
    val activity = LocalActivity.current as? ComponentActivity

    LaunchedEffect(Unit) {
        activity?.intent?.let { navController.handleDeepLink(it) }
    }

    DisposableEffect(activity) {
        val listener = Consumer<Intent> { intent -> navController.handleDeepLink(intent) }
        activity?.addOnNewIntentListener(listener)
        onDispose { activity?.removeOnNewIntentListener(listener) }
    }

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
