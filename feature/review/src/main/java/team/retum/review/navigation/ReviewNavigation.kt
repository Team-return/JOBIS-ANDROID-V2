package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.Review

const val NAVIGATION_REVIEW = "review"

fun NavGraphBuilder.review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(route = NAVIGATION_REVIEW) {
        Review(
            onReviewFilterClick = onReviewFilterClick,
            onSearchReviewClick = onSearchReviewClick,
            onReviewDetailClick = onReviewDetailClick,
        )
    }
}

fun NavController.navigateToReview() {
    navigate(NAVIGATION_REVIEW) {
        launchSingleTop = true
    }
}
