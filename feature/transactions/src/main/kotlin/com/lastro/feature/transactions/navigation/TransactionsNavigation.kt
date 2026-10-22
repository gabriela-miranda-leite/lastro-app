package com.lastro.feature.transactions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lastro.core.common.navigation.Route
import com.lastro.feature.transactions.ui.TransactionFormScreen
import com.lastro.feature.transactions.ui.TransactionsScreen

fun NavGraphBuilder.transactionsGraph(navController: NavController) {
    composable(Route.Transactions.List.path) {
        TransactionsScreen(
            onNavigateToForm = { id ->
                navController.navigate(Route.Transactions.Form(id).path)
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(
        route = "transactions/form/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        TransactionFormScreen(
            transactionId = id,
            onBack = { navController.popBackStack() }
        )
    }

    composable(Route.Transactions.Form().path) {
        TransactionFormScreen(
            transactionId = null,
            onBack = { navController.popBackStack() }
        )
    }
}
