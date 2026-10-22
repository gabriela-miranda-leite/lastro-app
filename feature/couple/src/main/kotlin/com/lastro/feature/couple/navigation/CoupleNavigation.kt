package com.lastro.feature.couple.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lastro.core.common.navigation.Route
import com.lastro.feature.couple.ui.CoupleHomeScreen
import com.lastro.feature.couple.ui.CoupleInviteScreen

fun NavGraphBuilder.coupleGraph(navController: NavController) {
    composable(Route.Couple.Home.path) {
        CoupleHomeScreen(
            onNavigateToInvite = {
                navController.navigate(Route.Couple.Invite.path)
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(Route.Couple.Invite.path) {
        CoupleInviteScreen(
            onBack = { navController.popBackStack() }
        )
    }
}
