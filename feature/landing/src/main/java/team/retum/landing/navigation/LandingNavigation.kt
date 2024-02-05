package team.retum.landing.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.landing.ui.Landing

const val NAVIGATION_LANDING = "landing"

fun NavGraphBuilder.landing(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    composable(route = NAVIGATION_LANDING) {
        Landing(
            onSignInClick = onSignInClick,
            onSignUpClick = onSignUpClick,
        )
    }
}
