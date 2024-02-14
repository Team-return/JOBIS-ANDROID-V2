package team.retum.jobis.verify_email.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.verify_email.ui.VerifyEmail

const val NAVIGATION_VERIFY_EMAIL = "verifyEmail"

fun NavGraphBuilder.verifyEmail(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_VERIFY_EMAIL) {
        VerifyEmail(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToVerifyEmail() {
    navigate(NAVIGATION_VERIFY_EMAIL)
}
