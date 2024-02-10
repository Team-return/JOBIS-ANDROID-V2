package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.InputPersonalInfo

const val NAVIGATION_INPUT_PERSONAL_INFO = "inputPersonalInfo"

fun NavGraphBuilder.inputPersonalInfo(
    onBackPressed: () -> Unit,
    navigateToInputEmail: (String, Long) -> Unit,
) {
    composable(NAVIGATION_INPUT_PERSONAL_INFO) {
        InputPersonalInfo(
            onBackPressed = onBackPressed,
            navigateToInputEmail = navigateToInputEmail,
        )
    }
}

fun NavController.navigateToInputPersonalInfo() {
    navigate(NAVIGATION_INPUT_PERSONAL_INFO)
}
