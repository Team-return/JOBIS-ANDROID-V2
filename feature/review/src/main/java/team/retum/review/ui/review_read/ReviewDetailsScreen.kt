package team.retum.review.ui.review_read

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.review.viewmodel.ReviewDetailsViewModel

@Composable
internal fun ReviewDetails(
    reviewId: Long,
    onBackPressed: () -> Unit,
    reviewDetailsViewModel: ReviewDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        reviewDetailsViewModel.setReviewId(reviewId.toString())
        reviewDetailsViewModel.fetchReviewDetails()
    }

    ReviewDetailsScreen()
}

@Composable
private fun ReviewDetailsScreen() {

}
