package team.retum.review.navigation.review_read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.review_read.Reviews
import team.retum.review.ui.review_read.SearchReviews

const val NAVIGATION_SEARCH_REVIEWS = "search_reviews"

fun NavGraphBuilder.searchReviews(
) {
    composable(
        route = NAVIGATION_SEARCH_REVIEWS,
    ) {
        SearchReviews()
    }
}

fun NavController.navigateToSearchReviews() {
    navigate(NAVIGATION_REVIEWS_FILTER)
}
