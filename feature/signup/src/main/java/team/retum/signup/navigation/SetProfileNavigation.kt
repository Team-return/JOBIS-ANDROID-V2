package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toJsonString
import team.retum.signup.ui.SetProfile

const val NAVIGATION_SET_PROFILE = "setProfile"

fun NavGraphBuilder.setProfile(
    onBackPressed: () -> Unit,
    navigateToTerms: (SignUpData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_SET_PROFILE/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { NavType.StringType }),
    ) {
        SetProfile(
            onBackPressed = onBackPressed,
            navigateToTerms = navigateToTerms,
            signUpData = it.getSignUpData(),
        )
    }
}

fun NavController.navigateToSetProfile(signUpData: SignUpData) {
    navigate("${NAVIGATION_SET_PROFILE}/${signUpData.toJsonString()}")
}
