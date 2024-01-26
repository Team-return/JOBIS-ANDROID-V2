package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(
    onBackClick: () -> Unit,
    onEmailClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO,
    ) {
        inputPersonalInformation(
            onBackClick = onBackClick,
            onEmailClick = onEmailClick,
        )
        inputEmail(onBackClick = onBackClick)
    }
}

fun NavController.navigateToSignUp() {
    navigate(NAVIGATION_SIGN_UP)
}
