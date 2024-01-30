package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputEmailScreen

const val NAVIGATION_SIGN_UP_INPUT_EMAIL = "signUp/inputEmail"

fun NavGraphBuilder.inputEmail(
    navController: NavController,
    onBackClick: () -> Unit,
) {
    composable(route = NAVIGATION_SIGN_UP_INPUT_EMAIL) {
        InputEmailScreen(navController = navController, onBackClick = onBackClick)
    }
}

fun NavController.navigateToInputEmail() {
    navigate(NAVIGATION_SIGN_UP_INPUT_EMAIL)
}