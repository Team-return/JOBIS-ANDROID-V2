package team.retum.jobis.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.interests.ui.InterestsComplete

const val NAVIGATION_INTERESTS_COMPLETE = "interestsComplete"
fun NavGraphBuilder.interestsComplete(
    onBackPressed: () -> Unit,
    navigateToMyPage: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_INTERESTS_COMPLETE/{${ResourceKeys.STUDENT_NAME}}",
        arguments = listOf(
            navArgument(ResourceKeys.STUDENT_NAME) { type = NavType.StringType }
        )
    ) {
        val studentName = it.arguments?.getString(ResourceKeys.STUDENT_NAME) ?: ""

        InterestsComplete(
            onBackPressed = onBackPressed,
            navigateToMyPage = navigateToMyPage,
            studentName = studentName,
        )
    }
}

fun NavController.navigateToInterestsComplete(studentName: String) {
    navigate("${NAVIGATION_INTERESTS_COMPLETE}/$studentName")
}
