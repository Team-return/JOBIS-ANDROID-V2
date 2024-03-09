package team.retum.landing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.landing.ui.Landing

const val NAVIGATION_LANDING = "landing"

fun NavGraphBuilder.landing(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    composable(NAVIGATION_LANDING) {
        Landing(
            onSignInClick = onSignInClick,
            onSignUpClick = onSignUpClick,
        )
    }
}

fun NavController.navigateToLanding(popUpRoute: String) {
    navigate(NAVIGATION_LANDING) {
        popUpTo(popUpRoute) {
            inclusive = true
        }
    }
}
