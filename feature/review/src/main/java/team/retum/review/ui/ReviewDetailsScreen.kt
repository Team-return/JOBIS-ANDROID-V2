package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.review.viewmodel.ReviewDetailsViewModel
import team.retum.usecase.entity.FetchReviewDetailEntity

/**
 * Displays the review details screen for the specified review and coordinates loading and tab interactions.
 *
 * When `reviewId` changes, the composable requests the review details from the associated view model.
 * The UI presents two tabs ("면접 후기", "예상 질문") and ignores tab selection if the current review's
 * question or answer is blank. Invokes `onBackPressed` when the user requests navigation back.
 *
 * @param reviewId The identifier of the review to display.
 * @param onBackPressed Callback invoked when the back action is requested.
 */
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
    val context = LocalContext.current

    LaunchedEffect(reviewId) {
        // TODO : 실 값 들어왔을 때 UI 호출
        reviewDetailsViewModel.setReviewId(reviewId.toString())
        reviewDetailsViewModel.fetchReviewDetails()
    }

    ReviewDetailsScreen(
        reviewDetail = state.reviewDetail,
        tabs = tabs.toPersistentList(),
        selectedTabIndex = state.selectedTabIndex,
        onSelectTab = {
            if (state.reviewDetail.answer.isBlank() || state.reviewDetail.question.isBlank()) {
                return@ReviewDetailsScreen
            }
            reviewDetailsViewModel.setTabIndex(it)
        },
        onBackPressed = onBackPressed,
    )
}

/**
 * Render the review details screen with a top app bar, tab bar, student information, and content for the selected tab.
 *
 * @param reviewDetail The fetched review data to display.
 * @param tabs Immutable list of tab labels.
 * @param selectedTabIndex The index of the currently selected tab.
 * @param onSelectTab Callback invoked with the new tab index when the user selects a tab.
 * @param onBackPressed Callback invoked when the back action is triggered.
 *
 * When `selectedTabIndex` is 0 the interview Q&A list is shown; when it is 1 the expected question/answer view is shown.
 */
@Composable
private fun ReviewDetailsScreen(
    reviewDetail: FetchReviewDetailEntity,
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
        StudentInfo(
            writer = reviewDetail.writer,
            major = reviewDetail.major,
            year = reviewDetail.year.toString(),
            companyName = reviewDetail.companyName,
            location = reviewDetail.location,
            type = reviewDetail.type,
            interviewerCount = reviewDetail.interviewerCount.toString(),
            selectedTabIndex = selectedTabIndex,
        )
        when (selectedTabIndex) {
            0 -> InterviewReview(review = reviewDetail.qnaResponse)
            1 -> ExpectedReview(review = reviewDetail)
        }
    }
}

/**
 * Displays the review header with writer, academic info, company and interview metadata, and a tab-specific title.
 *
 * The displayed title switches between "면접 후기" and "예상 질문" based on [selectedTabIndex].
 *
 * @param selectedTabIndex Index of the currently selected tab; non-zero values show the "예상 질문" title, zero shows "면접 후기".
 * @param interviewerCount The number of interviewers as a displayable string (e.g., "3").
 */
@Composable
private fun StudentInfo(
    writer: String,
    major: String,
    year: String,
    companyName: String,
    location: InterviewLocation,
    type: InterviewType,
    interviewerCount: String,
    selectedTabIndex: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            JobisText(
                text = "${writer}님의 ${if (selectedTabIndex != 0) {"예상 질문"} else {"면접 후기"}}",
                style = JobisTypography.PageTitle,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                JobisText(
                    text = major,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onPrimary,
                )
                JobisText(
                    text = year,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val type = when (type) {
                InterviewType.INDIVIDUAL -> "개인 면접"
                InterviewType.GROUP -> "단체 면접"
                InterviewType.OTHER -> "기타 면접"
            }
            val location = when (location) {
                InterviewLocation.DAEJEON -> "대전"
                InterviewLocation.SEOUL -> "서울"
                InterviewLocation.GYEONGGI -> "경기"
                InterviewLocation.OTHER -> "기타"
            }
            val items = listOf(companyName, location, type, "면접관 수 : $interviewerCount")

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

/**
 * Displays the interview review section consisting of a list of question-and-answer items.
 *
 * @param review The list of Q&A entries to render in the interview review. Each item represents a question and its corresponding answer.
 */
@Composable
private fun InterviewReview(
    review: List<FetchReviewDetailEntity.QnAs>,
) {
    ReviewContent(
        review = review,
    )
}

/**
 * Displays the expected interview question and its answer inside a styled card.
 *
 * Renders the question prefixed with a prominent "Q" label and the answer prefixed with an "A" label,
 * applying the screen's typography, color, and layout constraints.
 *
 * @param review FetchReviewDetailEntity containing the `question` and `answer` to present. 
 */
@Composable
private fun ExpectedReview(
    review: FetchReviewDetailEntity,
) {
    JobisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 4.dp,
            )
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.surfaceVariant),
        onClick = {},
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary, fontSize = 24.sp)) {
                                append("Q ")
                            }
                            withStyle(style = SpanStyle(color = JobisTheme.colors.onBackground, fontSize = 16.sp)) {
                                append(review.question)
                            }
                        },
                        style = JobisTypography.SubHeadLine,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary, fontSize = 24.sp)) {
                            append("A ")
                        }
                        withStyle(style = SpanStyle(color = JobisTheme.colors.inverseOnSurface, fontSize = 14.sp)) {
                            append(review.answer)
                        }
                    },
                    style = JobisTypography.Description,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(0.5f),
                    maxLines = 3,
                )
            }
        }
    }
}

/**
 * Renders a list of Q&A items as expandable cards where each card shows the question and, when expanded, its answer.
 *
 * Each item is displayed inside a card that toggles between a compact question-only view and an expanded view
 * containing both the question (prefixed with "Q") and the answer (prefixed with "A").
 *
 * @param review The list of Q&A entities to render; each element's `question` is shown in the card header and
 * `answer` is shown when that card is expanded.
 */
@Composable
private fun ReviewContent(
    review: List<FetchReviewDetailEntity.QnAs>,
) {
    review.forEachIndexed { index, reviewItem ->
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
                        Row {
                            JobisText(
                                text = reviewItem.question,
                                textAlign = TextAlign.Center,
                                style = JobisTypography.SubHeadLine,
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                contentDescription = "arrow_down",
                                modifier = Modifier.align(Alignment.CenterVertically),
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            JobisText(
                                text = "Q",
                                color = JobisTheme.colors.onPrimary,
                                style = JobisTypography.SubHeadLine.copy(fontSize = 24.sp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            JobisText(
                                text = reviewItem.question,
                                color = JobisTheme.colors.onBackground,
                                style = JobisTypography.SubHeadLine,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                contentDescription = "arrow_down",
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            JobisText(
                                text = "A",
                                color = JobisTheme.colors.onPrimary,
                                style = JobisTypography.Description.copy(fontSize = 24.sp),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            JobisText(
                                text = reviewItem.answer,
                                color = JobisTheme.colors.inverseOnSurface,
                                style = JobisTypography.Description,
                                textAlign = TextAlign.Center,
                                maxLines = 3,
                            )
                        }
                    }
                }
            }
        }
    }
}