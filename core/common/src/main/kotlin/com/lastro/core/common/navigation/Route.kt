package com.lastro.core.common.navigation

sealed class Route(val path: String) {

    data object Splash : Route("splash")

    sealed class Auth(path: String) : Route(path) {
        data object Welcome : Auth("auth/welcome")
        data object Login : Auth("auth/login")
        data object Register : Auth("auth/register")
        data object ForgotPassword : Auth("auth/forgot-password")
        data class ResetPassword(val token: String = "{token}") : Auth("auth/reset-password/${token}")
        data object OnboardingIncome : Auth("auth/onboarding/income")
    }

    data object Dashboard : Route("dashboard")

    sealed class Transactions(path: String) : Route(path) {
        data object List : Transactions("transactions")
        data class Form(val id: String? = null) : Transactions(
            if (id != null) "transactions/form/$id" else "transactions/form"
        )
    }

    sealed class Couple(path: String) : Route(path) {
        data object Home : Couple("couple")
        data object Invite : Couple("couple/invite")
    }

    data object Profile : Route("profile")
}
