package team.retum.jobis.change_password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.change_password.ui.ConfirmPassword

const val NAVIGATION_CONFIRM_PASSWORD = "confirmPassword"

fun NavGraphBuilder.confirmPassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_CONFIRM_PASSWORD) {
        ConfirmPassword(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToConfirmPassword() {
    navigate(NAVIGATION_CONFIRM_PASSWORD)
}
