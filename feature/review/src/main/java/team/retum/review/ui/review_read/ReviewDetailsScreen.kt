package team.retum.review.ui.review_read

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.review.viewmodel.ReviewDetailsViewModel
import team.retum.usecase.entity.FetchReviewDetailEntity
import team.retum.usecase.entity.PostReviewEntity

@Composable
internal fun ReviewDetails(
    reviewId: Long,
    onBackPressed: () -> Unit,
    reviewDetailsViewModel: ReviewDetailsViewModel = hiltViewModel(),
) {
    val tabs = listOf(
        "면접 후기",
        "예상 질문",
    )
    val state by reviewDetailsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        reviewDetailsViewModel.setReviewId(reviewId.toString())
        reviewDetailsViewModel.fetchReviewDetails()
    }

    ReviewDetailsScreen(
        tabs = tabs.toPersistentList(),
        selectedTabIndex = state.selectedTabIndex,
        onSelectTab = { reviewDetailsViewModel.setTabIndex(it) },
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun ReviewDetailsScreen(
    tabs: ImmutableList<String>,
    selectedTabIndex: Int,
    onSelectTab: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            title = "면접후기 상세보기",
            onBackPressed = onBackPressed,
        )
        TabBar(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs,
            onSelectTab = onSelectTab,
        )
        StudentInfo()
        InterviewReview()
        ExpectedReview()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StudentInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = "손희찬님의 면접 후기",
                style = JobisTypography.PageTitle,
            )
            JobisText(
                text = "Design",
                style = JobisTypography.Description,
                color = JobisTheme.colors.onPrimary,
            )
            JobisText(
                text = "2025",
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val items = listOf("ibk 기업은행", "대전", "개인 면접", "면접관 수 : 11명")

            items.forEachIndexed { index, item ->
                JobisText(
                    text = item,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.inverseOnSurface,
                )

                if (index < items.size - 1) {
                    JobisText(
                        text = "•",
                        style = JobisTypography.SubBody,
                        color = JobisTheme.colors.inverseOnSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewReview() {
    ReviewContent(
        review = PostReviewEntity.PostReviewContentEntity(
            question = "질문",
            answer = "답변",
            codeId = 1,
        ),
        keyword = "",
    )
}

@Composable
private fun ExpectedReview() {
}

@Composable
private fun ReviewContent(
    review: PostReviewEntity.PostReviewContentEntity,
    keyword: String,
) {
    var showQuestionDetail by remember { mutableStateOf(false) }
    JobisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 4.dp,
            )
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.surfaceVariant),
        onClick = { showQuestionDetail = !showQuestionDetail },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
        ) {
            Column {
                if (!showQuestionDetail) {
                    JobisText(
                        text = review.question,
                        style = JobisTypography.SubHeadLine,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    JobisText(
                        text = keyword,
                        style = JobisTypography.Description,
                        color = JobisTheme.colors.onPrimary,
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary)) {
                                append("Q ")
                            }
                            withStyle(style = SpanStyle(color = JobisTheme.colors.onBackground)) {
                                append(review.question)
                            }
                        },
                        style = JobisTypography.SubHeadLine,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    JobisText(
                        text = keyword,
                        style = JobisTypography.Description,
                        color = JobisTheme.colors.onPrimary,
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary)) {
                                append("A ")
                            }
                            withStyle(style = SpanStyle(color = JobisTheme.colors.inverseOnSurface)) {
                                append(review.answer)
                            }
                        },
                        style = JobisTypography.Description,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(0.5f),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "arrow_down",
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}
