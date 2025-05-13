package team.retum.jobis.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.interests.ui.Interests
import team.retum.jobis.interests.ui.InterestsComplete

const val NAVIGATION_INTERESTS_COMPLETE = "interestsComplete"
fun NavGraphBuilder.interestsComplete(onBackPressed: () -> Unit) {
    composable(NAVIGATION_INTERESTS_COMPLETE) {
        InterestsComplete(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToInterestsComplete() {
    navigate(NAVIGATION_INTERESTS_COMPLETE)
}
