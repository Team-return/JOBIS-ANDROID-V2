package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputPersonalInfoScreen

const val NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO = "signUp/inputPersonalInfo"

fun NavGraphBuilder.inputPersonalInformation(
    onBackClick: () -> Unit,
) {
    composable(route = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO) {
        InputPersonalInfoScreen(onBackClick = onBackClick)
    }
}

fun NavController.navigateToInputPersonalInfo() {
    navigate(NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO)
}
