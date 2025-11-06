package team.retum.jobis.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.interests.ui.Interests

const val NAVIGATION_INTERESTS = "interests"
fun NavGraphBuilder.interests(
    onBackPressed: () -> Unit,
    navigateToInterestsComplete: (String) -> Unit,
) {
    composable(NAVIGATION_INTERESTS) {
        Interests(
            onBackPressed = onBackPressed,
            navigateToInterestsComplete = navigateToInterestsComplete,
        )
    }
}

fun NavController.navigateToInterests() {
    navigate(NAVIGATION_INTERESTS)
}
