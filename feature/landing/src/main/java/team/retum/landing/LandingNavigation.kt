package team.retum.landing

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

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
