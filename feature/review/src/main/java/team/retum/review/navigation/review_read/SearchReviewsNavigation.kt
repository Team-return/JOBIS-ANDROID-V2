package team.retum.review.navigation.review_read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.review_read.SearchReview

const val NAVIGATION_SEARCH_REVIEW = "searchReview"

fun NavGraphBuilder.searchReview(
    onBackPressed: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_SEARCH_REVIEW,
    ) {
        SearchReview(
            onBackPressed = onBackPressed,
            onReviewDetailClick = onReviewDetailClick,
        )
    }
}

fun NavController.navigateToSearchReview() {
    navigate(NAVIGATION_SEARCH_REVIEW)
}
