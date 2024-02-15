package team.retum.jobis.change_password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.change_password.ui.ResetPassword

const val NAVIGATION_RESET_PASSWORD = "resetPassword"

fun NavGraphBuilder.resetPassword(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    composable(NAVIGATION_RESET_PASSWORD) {
        ResetPassword(
            onBackPressed = onBackPressed,
            onCompleteClick = onCompleteClick,
        )
    }
}

fun NavController.navigateToResetPassword() {
    navigate(NAVIGATION_RESET_PASSWORD)
}
