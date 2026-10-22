package com.lastro.core.common.navigation

sealed class Route(val path: String) {

    sealed class Auth(path: String) : Route(path) {
        data object Login : Auth("auth/login")
        data object Register : Auth("auth/register")
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
