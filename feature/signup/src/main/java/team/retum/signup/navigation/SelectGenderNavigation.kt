package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toJsonString
import team.retum.signup.ui.SelectGender

const val NAVIGATION_SELECT_GENDER = "selectGender"

fun NavGraphBuilder.selectGender(
    onBackPressed: () -> Unit,
    navigateToSetProfile: (SignUpData) -> Unit,
) {
    composable(
        route = "${NAVIGATION_SELECT_GENDER}/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { NavType.StringType }),
    ) {
        SelectGender(
            onBackPressed = onBackPressed,
            navigateToSetProfile = navigateToSetProfile,
            signUpData = it.getSignUpData(),
        )
    }
}

fun NavController.navigateToSelectGender(signUpData: SignUpData) {
    navigate("$NAVIGATION_SELECT_GENDER/${signUpData.toJsonString()}")
}
