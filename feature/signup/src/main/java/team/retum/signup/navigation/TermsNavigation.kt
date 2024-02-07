package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.Terms

const val NAVIGATION_TERMS = "terms"

fun NavGraphBuilder.terms(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    composable(NAVIGATION_TERMS) {
        Terms(
            onBackPressed = onBackPressed,
            onCompleteClick = onCompleteClick,
        )
    }
}

fun NavController.navigateToTerms() {
    navigate(NAVIGATION_TERMS)
}
