package team.retum.review.ui.review_write

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.review.viewmodel.PostNextViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.usecase.entity.QuestionsEntity

@Composable
internal fun PostNextReview(
    postNextViewModel: PostNextViewModel = hiltViewModel()
) {
    val state by postNextViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(Unit) {
        postNextViewModel.fetchQuestions()
    }

    Log.d("TEST", "questions : ${state.questions} question : ${state.questions}")
    PostNextReviewScreen(
        questions = state.questions,
        pagerState = pagerState
    )
}

@Composable
private fun PostNextReviewScreen(
    questions: List<QuestionsEntity.QuestionEntity>,
    pagerState: PagerState,
) {
    questions.forEachIndexed { index, _ ->
        Log.d("TEST", "questions : ${questions[index]}")
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
        ) { page ->
            JobisButton(
                text = questions[index].question,
                onClick = {
                    pagerState.pageCount + 1
                }
            )
        }
    }
}
