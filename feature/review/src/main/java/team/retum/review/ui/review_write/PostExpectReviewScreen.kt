package team.retum.review.ui.review_write

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.review.model.PostReviewData
import team.retum.review.viewmodel.PostExpectReviewSideEffect
import team.retum.review.viewmodel.PostExpectReviewState
import team.retum.review.viewmodel.PostExpectReviewViewModel
import team.retum.review.viewmodel.PostReviewSideEffect
import team.retum.review.viewmodel.PostReviewViewModel

@Composable
internal fun PostExpectReview(
    reviewData: PostReviewData,
    onBackPressed: () -> Unit,
    onPostReviewCompleteClick: () -> Unit,
    postExpectReviewViewModel: PostExpectReviewViewModel = hiltViewModel(),
) {
    val postReviewViewModel: PostReviewViewModel = hiltViewModel()
    val state by postExpectReviewViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        postExpectReviewViewModel.sideEffect.collect {
            when (it) {
                is PostExpectReviewSideEffect.PostReview -> {
                    postReviewViewModel.postReview(
                        reviewData = reviewData.copy(
                            question = state.question,
                            answer = state.answer,
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        postReviewViewModel.sideEffect.collect {
            if (it is PostReviewSideEffect.Success)
                onPostReviewCompleteClick
        }
    }

    PostExpectReviewScreen(
        onBackPressed = onBackPressed,
        onReviewFinishClick = postExpectReviewViewModel::setEmpty,
        answer = { state.answer },
        onAnswerChange = postExpectReviewViewModel::setAnswer,
        question = { state.question },
        onQuestionChange = postExpectReviewViewModel::setQuestion,
        state = state,
    )
}

@Composable
private fun PostExpectReviewScreen(
    onBackPressed: () -> Unit,
    onReviewFinishClick: () -> Unit,
    answer: () -> String,
    onAnswerChange: (String) -> Unit,
    question: () -> String,
    onQuestionChange: (String) -> Unit,
    state: PostExpectReviewState,
) {
    Column{
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
        )
        JobisText(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 24.dp),
            text = "받았던 면접 질문을 추가해주세요!",
            style = JobisTypography.PageTitle,
        )
        Row(
            modifier = Modifier.padding(top = 30.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            JobisText(
                text = "질문",
                style = JobisTypography.Description
            )
            JobisText(
                text = " *",
                style = JobisTypography.Description,
                color = JobisTheme.colors.onPrimary,
            )
        }
        JobisTextField(
            value = answer,
            onValueChange = onAnswerChange,
            hint = "받았던 질문을 작성해 주세요.",
        )
        Row(
            modifier = Modifier.padding(top = 30.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            JobisText(
                text = stringResource(R.string.answer),
                style = JobisTypography.Description
            )
            JobisText(
                text = " *",
                style = JobisTypography.Description,
                color = JobisTheme.colors.onPrimary,
            )
        }
        JobisTextField(
            modifier = Modifier.heightIn(min = 120.dp, max = 300.dp),
            value = question,
            onValueChange = onQuestionChange,
            hint = "예상 질문 답변을 성심성의껏 작성해 주세요!",
            singleLine = false,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp)
                .clickable { onReviewFinishClick() },
            text = "건너뛸래요.",
            style = JobisTypography.SubBody,
            textDecoration = TextDecoration.Underline,
            color = JobisTheme.colors.surfaceTint,
        )
        JobisButton(
            text = "완료",
            color = ButtonColor.Primary,
            onClick = onReviewFinishClick,
            enabled = state.buttonEnabled
        )
    }
}
