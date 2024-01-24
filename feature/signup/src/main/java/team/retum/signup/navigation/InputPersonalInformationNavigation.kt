package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputPersonalInformationScreen

const val NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFORMATION = "signUp/inputPersonalInformation"

fun NavGraphBuilder.inputPersonalInformation(
    onBackClick: () -> Unit,
) {
    composable(route = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFORMATION) {
        InputPersonalInformationScreen(onBackClick = onBackClick)
    }
}

fun NavController.navigateToInputPersonalInformation() {
    navigate(NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFORMATION)
}
