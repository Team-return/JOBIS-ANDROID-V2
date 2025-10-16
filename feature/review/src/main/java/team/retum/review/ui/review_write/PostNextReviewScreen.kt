package team.retum.review.ui.review_write

import android.util.Log
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
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.review.viewmodel.PostNextViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.QuestionsEntity

@Composable
internal fun PostNextReview(
    onBackPressed: () -> Unit,
    onPostExpectReviewClick: () -> Unit,
    postNextViewModel: PostNextViewModel = hiltViewModel()
) {
    val state by postNextViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { state.questions.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        postNextViewModel.fetchQuestions()
    }

    PostNextReviewScreen(
        onBackPressed = onBackPressed,
        questions = state.questions,
        coroutineScope = coroutineScope,
        pagerState = pagerState,
        answer = { state.answer },
        onAnswerChange = postNextViewModel::setAnswer,
        onPostExpectReviewClick = onPostExpectReviewClick,
    )
}

@Composable
private fun PostNextReviewScreen(
    onBackPressed: () -> Unit,
    questions: List<QuestionsEntity.QuestionEntity>,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    answer: () -> String,
    onAnswerChange: (String) -> Unit,
    onPostExpectReviewClick: () -> Unit,
) {
    Log.d("TEST", pagerState.currentPage.toString())
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        JobisSmallTopAppBar(
            onBackPressed = {
                if (pagerState.currentPage == 0) onBackPressed() else coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
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
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(pagerState.pageCount) {
                            val color = if (pagerState.currentPage == it)
                                JobisTheme.colors.onPrimary
                            else
                                JobisTheme.colors.surfaceVariant
                            val multiple = if (pagerState.currentPage == it)
                                1.8f
                            else
                                1f
                            Box(
                                modifier = Modifier
                                    .background(color = color, shape = RoundedCornerShape(200.dp))
                                    .size(width = 12.dp * multiple, height = 6.dp)
                            )
                        }
                    }
                    JobisText(
                        modifier = Modifier.padding(vertical = 18.dp),
                        text = questions[page].question,
                        style = JobisTypography.PageTitle,
                    )
                    Row(
                        modifier = Modifier.padding(top = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.answer),
                            style = JobisTypography.Description
                        )
                        JobisText(
                            text = "*",
                            style = JobisTypography.Description,
                            color = JobisTheme.colors.onPrimary,
                        )
                    }
                }
                JobisTextField(
                    modifier = Modifier.heightIn(min = 120.dp, max = 300.dp),
                    value = answer,
                    onValueChange = onAnswerChange,
                    hint = stringResource(R.string.answer_review),
                    singleLine = false,
                )
                Spacer(modifier = Modifier.weight(1f))
                JobisButton(
                    text = "다음",
                    color = ButtonColor.Primary,
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage != 2) pagerState.animateScrollToPage(pagerState.currentPage + 1) else onPostExpectReviewClick
                        }
                    }
                )
            }
        }
    }
}
