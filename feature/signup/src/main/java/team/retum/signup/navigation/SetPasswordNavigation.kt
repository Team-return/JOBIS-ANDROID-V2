package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toJsonString
import team.retum.signup.ui.SetPassword

const val NAVIGATION_SET_PASSWORD = "setPassword"

fun NavGraphBuilder.setPassword(
    onBackPressed: () -> Unit,
    navigateToSelectGender: (SignUpData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_SET_PASSWORD/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { type = NavType.StringType }),
    ) {
        SetPassword(
            onBackPressed = onBackPressed,
            navigateToSelectGender = navigateToSelectGender,
            signUpData = it.getSignUpData(),
        )
    }
}

fun NavController.navigateToSetPassword(signUpData: SignUpData) {
    navigate("$NAVIGATION_SET_PASSWORD/${signUpData.toJsonString()}")
}
