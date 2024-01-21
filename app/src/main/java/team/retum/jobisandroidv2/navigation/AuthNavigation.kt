package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.landing.NAVIGATION_LANDING
import team.retum.landing.landing

const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation() {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = NAVIGATION_LANDING,
    ) {
        landing()
    }
}
