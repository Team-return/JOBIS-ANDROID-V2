package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toJsonString
import team.retum.signup.ui.InputEmail

const val NAVIGATION_INPUT_EMAIL = "inputEmail"

fun NavGraphBuilder.inputEmail(
    onBackPressed: () -> Unit,
    navigateToSetPassword: (SignUpData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_INPUT_EMAIL/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { type = NavType.StringType }),
    ) {
        InputEmail(
            onBackPressed = onBackPressed,
            navigateToSetPassword = navigateToSetPassword,
            signUpData = it.getSignUpData(),
        )
    }
}

fun NavController.navigateToInputEmail(signUpData: SignUpData) {
    navigate("$NAVIGATION_INPUT_EMAIL/${signUpData.toJsonString()}")
}
