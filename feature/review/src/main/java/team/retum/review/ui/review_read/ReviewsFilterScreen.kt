package team.retum.review.ui.review_read

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
internal fun ReviewsFilter(
    onBackPressed: () -> Unit
) {
    ReviewsFilterScreen()
}

@Composable
private fun ReviewsFilterScreen() {
    Text("ReviewsFilterScreen")
}
