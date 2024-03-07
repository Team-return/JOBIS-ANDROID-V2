package team.retum.jobis.change_password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.change_password.ui.ResetPassword

const val NAVIGATION_RESET_PASSWORD = "resetPassword"

private const val TYPE = "{type}"
private const val VALUE = "{value}"

fun NavGraphBuilder.resetPassword(
    onBackPressed: () -> Unit,
    navigateToSignIn: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_RESET_PASSWORD/$TYPE/$VALUE",
        arguments = listOf(
            navArgument(ResourceKeys.TYPE) { NavType.StringType },
            navArgument(ResourceKeys.VALUE) { NavType.StringType },
        ),
    ) {
        it.arguments?.run {
            val type = getString(ResourceKeys.TYPE)
            val value = getString(ResourceKeys.VALUE)
            ResetPassword(
                onBackPressed = onBackPressed,
                navigateToSignIn = navigateToSignIn,
                email = if (type == ResetPasswordNavigationArgumentType.EMAIL.toString()) {
                    value
                } else {
                    null
                },
                currentPassword = if (type == ResetPasswordNavigationArgumentType.PASSWORD.toString()) {
                    value
                } else {
                    null
                },
            )
        }
    }
}

fun NavController.navigateToResetPassword(
    type: ResetPasswordNavigationArgumentType,
    value: String,
) {
    navigate("$NAVIGATION_RESET_PASSWORD/$type/$value")
}
