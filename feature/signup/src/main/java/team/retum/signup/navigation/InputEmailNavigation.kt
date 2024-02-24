package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.ui.InputEmail

const val NAVIGATION_INPUT_EMAIL = "inputEmail"

fun NavGraphBuilder.inputEmail(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_INPUT_EMAIL/{${ResourceKeys.SIGN_UP}}",
        arguments = listOf(navArgument(ResourceKeys.SIGN_UP) { NavType.SerializableType(SignUpData::class.java) }),
    ) {
        InputEmail(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
            signUpData = it::getSignUpData,
        )
    }
}

fun NavController.navigateToInputEmail(
    name: String,
    number: Long,
) {
    val signUpData = SignUpData(
        name = name,
        number = number,
    )
    navigate("$NAVIGATION_INPUT_EMAIL/$signUpData")
}
