package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.Reviews

const val NAVIGATION_REVIEWS = "reviews"

fun NavGraphBuilder.reviews(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEWS,
    ) {
        Reviews(
            onReviewFilterClick = onReviewFilterClick,
            onSearchReviewClick = onSearchReviewClick,
            onReviewDetailClick = onReviewDetailClick
        )
    }
}

fun NavController.navigateToReviews() {
    navigate(NAVIGATION_REVIEWS)
}
