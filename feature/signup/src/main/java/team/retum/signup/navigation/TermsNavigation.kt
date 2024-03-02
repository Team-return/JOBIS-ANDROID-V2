package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toJsonString
import team.retum.signup.ui.Terms

const val NAVIGATION_TERMS = "terms"

fun NavGraphBuilder.terms(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_TERMS/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { NavType.StringType }),
    ) {
        Terms(
            onBackPressed = onBackPressed,
            onCompleteClick = onCompleteClick,
            signUpData = it.getSignUpData(),
        )
    }
}

fun NavController.navigateToTerms(signUpData: SignUpData) {
    navigate("${NAVIGATION_TERMS}/${signUpData.toJsonString()}")
}
