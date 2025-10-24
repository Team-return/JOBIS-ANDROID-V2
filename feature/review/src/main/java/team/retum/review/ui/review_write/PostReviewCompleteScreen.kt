package team.retum.review.ui.review_write

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.review.viewmodel.PostReviewViewModel

const val SCREEN_TIME = 1500L

@Composable
internal fun PostReviewComplete(
    navigateToPostReview: (String, Long) -> Unit,
    postReviewViewModel: PostReviewViewModel = hiltViewModel(),
) {
    val state by postReviewViewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        delay(SCREEN_TIME)
        navigateToPostReview(postReviewViewModel.companyName.value, postReviewViewModel.companyId.value)
    }

    PostReviewCompleteScreen(
        studentName = state.studentName,
    )
}

@Composable
private fun PostReviewCompleteScreen(
    studentName: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(team.retum.design_system.R.drawable.success),
                contentDescription = "review_make_success",
            )
            JobisText(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
                text = stringResource(R.string.post_complete_review_check_title, studentName),
                textAlign = TextAlign.Center,
                style = JobisTypography.PageTitle,
                color = JobisTheme.colors.onBackground,
            )
            JobisText(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 20.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
                text = stringResource(R.string.post_complete_review_good_result),
                textAlign = TextAlign.Center,
                style = JobisTypography.SubBody,
            )
        }
    }
}
