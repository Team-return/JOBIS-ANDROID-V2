package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.landing.NAVIGATION_LANDING
import team.retum.landing.landing
import team.retum.signin.navigateToSignIn
import team.retum.signin.signIn
import team.retum.signup.navigation.navigateToInputEmail
import team.retum.signup.navigation.navigateToInputPersonalInfo
import team.retum.signup.navigation.navigateToSignUp
import team.retum.signup.navigation.signUp


const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = NAVIGATION_LANDING,
    ) {
        landing(
            onSignInClick = navController::navigateToSignIn,
            onSignUpClick = navController::navigateToSignUp,
        )
        signIn(onBackClick = navController::popBackStack)
        signUp(
            navController = navController,
            onBackClick = navController::popBackStack,
            onNextClick = navController::navigateToInputEmail,
        )
    }
}
