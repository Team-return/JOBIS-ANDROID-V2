package team.retum.jobis.verify_email.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.jobis.verify_email.ui.VerifyEmail

const val NAVIGATION_VERIFY_EMAIL = "verifyEmail"

fun NavGraphBuilder.verifyEmail(
    onBackPressed: () -> Unit,
    navigateToResetPassword: (type: ResetPasswordNavigationArgumentType, email: String) -> Unit,
) {
    composable(NAVIGATION_VERIFY_EMAIL) {
        VerifyEmail(
            onBackPressed = onBackPressed,
            navigateToResetPassword = navigateToResetPassword,
        )
    }
}

fun NavController.navigateToVerifyEmail() {
    navigate(NAVIGATION_VERIFY_EMAIL)
}
