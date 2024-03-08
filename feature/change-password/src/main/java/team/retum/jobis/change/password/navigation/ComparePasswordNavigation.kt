package team.retum.jobis.change.password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.jobis.change.password.ui.ComparePassword

const val NAVIGATION_COMPARE_PASSWORD = "confirmPassword"

fun NavGraphBuilder.confirmPassword(
    onBackPressed: () -> Unit,
    navigateToResetPassword: (type: ResetPasswordNavigationArgumentType, currentPassword: String) -> Unit,
) {
    composable(NAVIGATION_COMPARE_PASSWORD) {
        ComparePassword(
            onBackPressed = onBackPressed,
            navigateToResetPassword = navigateToResetPassword,
        )
    }
}

fun NavController.navigateToComparePassword() {
    navigate(NAVIGATION_COMPARE_PASSWORD)
}
