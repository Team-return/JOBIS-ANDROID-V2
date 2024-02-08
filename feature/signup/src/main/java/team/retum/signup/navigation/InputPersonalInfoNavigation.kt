package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputPersonalInfo

const val NAVIGATION_INPUT_PERSONAL_INFO = "inputPersonalInfo"

fun NavGraphBuilder.inputPersonalInfo(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_INPUT_PERSONAL_INFO) {
        InputPersonalInfo(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToInputPersonalInfo() {
    navigate(NAVIGATION_INPUT_PERSONAL_INFO)
}
