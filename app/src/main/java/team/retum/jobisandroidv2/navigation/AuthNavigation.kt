package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.jobis.change.password.navigation.confirmPassword
import team.retum.jobis.change.password.navigation.resetPassword
import team.retum.jobis.splash.navigation.NAVIGATION_SPLASH
import team.retum.jobis.splash.navigation.splash
import team.retum.jobis.verify.email.navigation.verifyEmail
import team.retum.jobisandroidv2.JobisNavigator
import team.retum.landing.navigation.landing
import team.retum.signin.navigation.signIn
import team.retum.signup.navigation.signUp

const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation(
    navigator: JobisNavigator,
) {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = NAVIGATION_SPLASH,
    ) {
        splash(
            navigateToLanding = { navigator.navigateToLanding(NAVIGATION_SPLASH) },
            navigateToRoot = navigator::navigateToRoot,
        )
        landing(
            onSignInClick = navigator::navigateToSignIn,
            onSignUpClick = navigator::navigateToSignUp,
        )
        signIn(
            onBackPressed = navigator::popBackStackIfNotHome,
            onSignInSuccess = navigator::navigateToRoot,
            onForgotPasswordClick = navigator::navigateToVerifyEmail,
        )
        signUp(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToInputEmail = navigator::navigateToInputEmail,
            navigateToSetPassword = navigator::navigateToSetPassword,
            navigateToSelectGender = navigator::navigateToSelectGender,
            navigateToSetProfile = navigator::navigateToSetProfile,
            navigateToTerms = navigator::navigateToTerms,
            navigateToRoot = navigator::navigateToRoot,
        )
        verifyEmail(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToResetPassword = navigator::navigateToResetPassword,
        )
        resetPassword(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToSignIn = navigator::navigateToSignIn,
        )
        confirmPassword(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToResetPassword = navigator::navigateToResetPassword,
        )
    }
}
