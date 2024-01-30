package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(
    navController: NavController,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO,
    ) {
        inputPersonalInformation(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
        inputEmail( navController = navController, onBackClick = onBackClick)
        settingPassword(onBackClick = onBackClick)
    }
}

fun NavController.navigateToSignUp() {
    navigate(NAVIGATION_SIGN_UP)
}
