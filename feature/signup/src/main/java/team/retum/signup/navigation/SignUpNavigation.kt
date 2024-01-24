package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(onBackClick: () -> Unit) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFORMATION,
    ) {
        inputPersonalInformation(onBackClick = onBackClick)
    }
}

fun NavController.navigateToSignUp() {
    navigate(NAVIGATION_SIGN_UP)
}
