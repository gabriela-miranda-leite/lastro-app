package com.lastro.core.common.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink

const val LASTRO_DEEPLINK_SCHEME = "lastro"

fun deepLinksFor(path: String): List<NavDeepLink> = listOf(
    navDeepLink { uriPattern = "$LASTRO_DEEPLINK_SCHEME://$path" }
)
