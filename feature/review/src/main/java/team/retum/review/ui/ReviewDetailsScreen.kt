package team.retum.review.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.review.viewmodel.ReviewDetailsState
import team.retum.review.viewmodel.ReviewDetailsViewModel

@Composable
internal fun ReviewDetails(
    onBackPressed: () -> Unit,
    writer: String,
    reviewId: String,
    reviewDetailsViewModel: ReviewDetailsViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val state by reviewDetailsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        reviewDetailsViewModel.setReviewId(reviewId = reviewId)
        reviewDetailsViewModel.fetchReviewDetails()
    }

    ReviewDetailsScreen(
        onBackPressed = onBackPressed,
        writer = writer,
        scrollState = scrollState,
        state = state,
    )
}

@Composable
private fun ReviewDetailsScreen(
    onBackPressed: () -> Unit,
    writer: String,
    scrollState: ScrollState,
    state: ReviewDetailsState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            onBackPressed = onBackPressed,
            title = writer,
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
        ) {
            JobisText(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(id = R.string.review_questions),
                color = JobisTheme.colors.onSurfaceVariant,
                style = JobisTypography.Description,
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.questions.forEach {
                    val (showAnswer, setShowAnswer) = remember { mutableStateOf(false) }
                    ReviewQuestionContent(
                        question = it.question,
                        area = it.area,
                        answer = it.answer,
                        showAnswer = showAnswer,
                        onShowAnswerClick = setShowAnswer,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewQuestionContent(
    question: String,
    area: String,
    answer: String,
    showAnswer: Boolean,
    onShowAnswerClick: (Boolean) -> Unit,
) {
    val rotate by animateFloatAsState(
        targetValue = if (showAnswer) {
            180f
        } else {
            0f
        },
        label = "",
    )

    JobisCard(onClick = { onShowAnswerClick(!showAnswer) }) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Question(
                    question = question,
                    area = area,
                )
                Icon(
                    modifier = Modifier.rotate(rotate),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "down",
                    tint = JobisTheme.colors.onSurfaceVariant,
                )
            }
            AnimatedVisibility(visible = showAnswer) {
                Answer(answer = answer)
            }
        }
    }
}

@Composable
private fun RowScope.Question(
    question: String,
    area: String,
) {
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        JobisText(
            text = "Q",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.onPrimary,
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            JobisText(
                text = question,
                style = JobisTypography.SubHeadLine,
            )
            JobisText(
                text = area,
                style = JobisTypography.Description,
                color = JobisTheme.colors.secondary,
            )
        }
    }
}

@Composable
private fun Answer(answer: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        JobisText(
            text = "A",
            style = JobisTypography.SubHeadLine,
        )
        JobisText(
            text = answer,
            style = JobisTypography.Description,
            color = JobisTheme.colors.inverseOnSurface,
        )
    }
}
