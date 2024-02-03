package team.retum.signin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NAVIGATION_SIGN_IN = "signIn"

fun NavGraphBuilder.signIn(
    onBackClick: () -> Unit,
    onSignInSuccess: () -> Unit,
) {
    composable(route = NAVIGATION_SIGN_IN) {
        SignIn(
            onBackClick = onBackClick,
            onSignInSuccess = onSignInSuccess,
        )
    }
}

fun NavController.navigateToSignIn() {
    navigate(NAVIGATION_SIGN_IN)
}
