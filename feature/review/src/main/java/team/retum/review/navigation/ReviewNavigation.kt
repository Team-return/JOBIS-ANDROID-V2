package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.review.ui.Review

const val NAVIGATION_REVIEW = "review"
private const val ARG_CODE = "code"
private const val ARG_YEAR = "year"
private const val ARG_INTERVIEW_TYPE = "interviewType"
private const val ARG_LOCATION = "location"

private const val ROUTE_REVIEW = "$NAVIGATION_REVIEW?" +
    "$ARG_CODE={$ARG_CODE}&" +
    "$ARG_YEAR={$ARG_YEAR}&" +
    "$ARG_INTERVIEW_TYPE={$ARG_INTERVIEW_TYPE}&" +
    "$ARG_LOCATION={$ARG_LOCATION}"

fun NavGraphBuilder.review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = ROUTE_REVIEW,
        arguments = listOf(
            navArgument(ARG_CODE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(ARG_YEAR) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(ARG_INTERVIEW_TYPE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(ARG_LOCATION) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) { backStackEntry ->
        val code = backStackEntry.arguments?.getString(ARG_CODE)?.toLongOrNull()
        val year = backStackEntry.arguments?.getString(ARG_YEAR)?.toIntOrNull()
        val interviewType = backStackEntry.arguments?.getString(ARG_INTERVIEW_TYPE)
            ?.let { runCatching { InterviewType.valueOf(it) }.getOrNull() }
        val location = backStackEntry.arguments?.getString(ARG_LOCATION)
            ?.let { runCatching { InterviewLocation.valueOf(it) }.getOrNull() }

        Review(
            code = code,
            year = year,
            interviewType = interviewType,
            location = location,
            onReviewFilterClick = onReviewFilterClick,
            onSearchReviewClick = onSearchReviewClick,
            onReviewDetailClick = onReviewDetailClick,
        )
    }
}

fun NavController.navigateToReview(
    code: Long? = null,
    year: Int? = null,
    interviewType: InterviewType? = null,
    location: InterviewLocation? = null,
) {
    val route = buildString {
        append(NAVIGATION_REVIEW)
        append("?")
        append("$ARG_CODE=${code ?: ""}")
        append("&$ARG_YEAR=${year ?: ""}")
        append("&$ARG_INTERVIEW_TYPE=${interviewType?.name ?: ""}")
        append("&$ARG_LOCATION=${location?.name ?: ""}")
    }
    navigate(route) {
        popUpTo("root")
        launchSingleTop = true
    }
}
