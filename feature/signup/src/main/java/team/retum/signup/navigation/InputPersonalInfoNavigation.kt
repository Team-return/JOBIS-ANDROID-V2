package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputPersonalInfoScreen

const val NAVIGATION_INPUT_PERSONAL_INFO = "inputPersonalInfo"

fun NavGraphBuilder.inputPersonalInformation(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_INPUT_PERSONAL_INFO) {
        InputPersonalInfoScreen(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToInputPersonalInfo() {
    navigate(NAVIGATION_INPUT_PERSONAL_INFO)
}
