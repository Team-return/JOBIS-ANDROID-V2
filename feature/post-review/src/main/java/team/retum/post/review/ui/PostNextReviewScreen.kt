package team.retum.post.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.post.review.R
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.viewmodel.PostNextReviewSideEffect
import team.retum.post.review.viewmodel.PostNextReviewViewModel
import team.retum.usecase.entity.QuestionsEntity.QuestionEntity

@Composable
internal fun PostNextReview(
    reviewData: PostReviewData,
    onBackPressed: () -> Unit,
    navigateToPostExpectReview: (PostReviewData) -> Unit,
    postNextReviewViewModel: PostNextReviewViewModel = hiltViewModel(),
) {
    val state by postNextReviewViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { state.questions.size })
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        postNextReviewViewModel.fetchQuestions()
        postNextReviewViewModel.sideEffect.collect {
            when (it) {
                is PostNextReviewSideEffect.MoveToNext -> {
                    navigateToPostExpectReview(
                        reviewData.copy(qnaElements = state.qnaElements),
                    )
                }

                is PostNextReviewSideEffect.FetchQuestionError -> {
                    JobisToast.create(
                        message = "질문을 조회하지 못했어요",
                        context = context,
                    )
                }
            }
        }
    }

    PostNextReviewScreen(
        onBackPressed = onBackPressed,
        questions = state.questions,
        answers = state.answers,
        coroutineScope = coroutineScope,
        pagerState = pagerState,
        onPostExpectReviewClick = postNextReviewViewModel::onNextClick,
        setAnswer = postNextReviewViewModel::getAnswer,
        onAnswerChange = postNextReviewViewModel::setAnswer,
        setQuestion = postNextReviewViewModel::setQuestion,
    )
}

@Composable
private fun PostNextReviewScreen(
    onBackPressed: () -> Unit,
    questions: List<QuestionEntity>,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    answers: List<String>,
    onPostExpectReviewClick: () -> Unit,
    setAnswer: (Int) -> String,
    onAnswerChange: (String, Int) -> Unit,
    setQuestion: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        JobisSmallTopAppBar(
            onBackPressed = {
                if (pagerState.currentPage == 0) {
                    onBackPressed()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
            userScrollEnabled = false,
        ) { page ->
            Column {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        repeat(pagerState.pageCount) {
                            val color = if (pagerState.currentPage == it) JobisTheme.colors.onPrimary else JobisTheme.colors.surfaceVariant
                            val multiple = if (pagerState.currentPage == it) 1.8f else 1f
                            Box(
                                modifier = Modifier
                                    .background(color = color, shape = RoundedCornerShape(200.dp))
                                    .size(width = 12.dp * multiple, height = 6.dp),
                            )
                        }
                    }
                    JobisText(
                        modifier = Modifier.padding(vertical = 18.dp),
                        text = stringResource(id = R.string.question_prefix) + questions[page].question,
                        style = JobisTypography.PageTitle,
                    )
                    Row(
                        modifier = Modifier.padding(top = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        JobisText(
                            text = stringResource(R.string.answer),
                            style = JobisTypography.Description,
                        )
                        JobisText(
                            text = stringResource(id = R.string.required_mark),
                            style = JobisTypography.Description,
                            color = JobisTheme.colors.onPrimary,
                        )
                    }
                }
                JobisTextField(
                    modifier = Modifier.heightIn(min = 120.dp, max = 300.dp),
                    value = { setAnswer(page) },
                    onValueChange = { onAnswerChange(it, page) },
                    hint = stringResource(R.string.hint_answer_review),
                    singleLine = false,
                )
                Spacer(modifier = Modifier.weight(1f))
                JobisButton(
                    text = stringResource(id = R.string.next),
                    color = ButtonColor.Primary,
                    onClick = {
                        setQuestion()
                        coroutineScope.launch {
                            if (pagerState.currentPage != 2) pagerState.animateScrollToPage(pagerState.currentPage + 1) else onPostExpectReviewClick()
                        }
                    },
                )
            }
        }
    }
}
