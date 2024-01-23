package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.landing.NAVIGATION_LANDING
import team.retum.landing.landing
import team.retum.signin.navigateToSignIn
import team.retum.signin.signIn

const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = NAVIGATION_LANDING,
    ) {
        landing(onSignInClick = navController::navigateToSignIn)
        signIn(onBackClick = navController::popBackStack)
    }
}
