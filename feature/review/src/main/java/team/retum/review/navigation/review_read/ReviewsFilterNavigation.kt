package team.retum.review.navigation.review_read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.review_read.Reviews
import team.retum.review.ui.review_read.ReviewsFilter

const val NAVIGATION_REVIEWS_FILTER = "reviews_filter"

fun NavGraphBuilder.reviewsFilter(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEWS_FILTER,
    ) {
        ReviewsFilter()
    }
}

fun NavController.navigateToReviewsFilter() {
    navigate(NAVIGATION_REVIEWS_FILTER)
}
