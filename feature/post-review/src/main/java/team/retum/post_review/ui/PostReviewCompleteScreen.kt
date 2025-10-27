package team.retum.post_review.ui

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
import team.retum.post_review.R
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.post_review.viewmodel.PostReviewViewModel

const val SCREEN_TIME = 1500L

@Composable
internal fun PostReviewComplete(
    onBackPressed: () -> Unit,
    navigateToPostReview: (String, Long) -> Unit,
    postReviewViewModel: PostReviewViewModel = hiltViewModel(),
) {
    val state by postReviewViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // TODO :: 빈번하게 일어나는 api 요청 개선
        delay(SCREEN_TIME)
        onBackPressed()
        navigateToPostReview("", 0)
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
                painter = painterResource(team.retum.design_system.R.drawable.ic_success),
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
