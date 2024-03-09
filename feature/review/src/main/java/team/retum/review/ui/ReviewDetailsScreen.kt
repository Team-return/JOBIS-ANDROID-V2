package team.retum.review.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
internal fun ReviewDetails(
    onBackPressed: () -> Unit,
    writer: String,
    reviewId: String,
) {
    val scrollState = rememberScrollState()

    ReviewDetailsScreen(
        onBackPressed = onBackPressed,
        writer = writer,
        scrollState = scrollState,
    )
}

@Composable
private fun ReviewDetailsScreen(
    onBackPressed: () -> Unit,
    writer: String,
    scrollState: ScrollState,
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
            ReviewQuestionContent(
                question = "나에게 뭐든 물어봐",
                area = "서버 개발자",
                answer = "알잖아 나",
                showAnswer = true,
                onShowAnswerClick = {},
            )
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
    JobisCard {
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
                JobisIconButton(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "down",
                    onClick = { onShowAnswerClick(!showAnswer) },
                    tint = JobisTheme.colors.onSurfaceVariant,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
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
