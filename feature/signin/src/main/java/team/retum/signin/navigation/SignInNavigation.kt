package team.retum.signin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signin.ui.SignIn

const val NAVIGATION_SIGN_IN = "signIn"

fun NavGraphBuilder.signIn(
    onBackPressed: () -> Unit,
    onSignInSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    composable(NAVIGATION_SIGN_IN) {
        SignIn(
            onBackClick = onBackPressed,
            onSignInSuccess = onSignInSuccess,
            onForgotPasswordClick = onForgotPasswordClick,
        )
    }
}

fun NavController.navigateToSignIn() {
    navigate(NAVIGATION_SIGN_IN)
}
