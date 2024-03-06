package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.review.viewmodel.ReviewSideEffect
import team.retum.review.viewmodel.ReviewViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PostReview(
    onBackPressed: () -> Unit,
    companyId: Long,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val sheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val sheetScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        reviewViewModel.sideEffect.collect {
            if (it is ReviewSideEffect.Success) {
                JobisToast.create(
                    context = context,
                    message = context.getString(R.string.added_question),
                ).show()
            }
        }
    }

    PostReviewScreen(
        onBackPressed = onBackPressed,
        sheetScope = sheetScope,
        sheetState = sheetState,
        companyId = companyId,
        addQuestion = { id ->
            reviewViewModel.postReview(
                companyId = id,
                answer = state.answer,
                question = state.question,
                codeId = reviewViewModel.techs.toList()[0].code,
            )
        },
        question = state.question,
        answer = state.answer,
        setQuestion = reviewViewModel::setQuestion,
        setAnswer = reviewViewModel::setAnswer,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PostReviewScreen(
    onBackPressed: () -> Unit,
    sheetScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    companyId: Long,
    addQuestion: (Long) -> Unit,
    question: String,
    answer: String,
    setQuestion: (String) -> Unit,
    setAnswer: (String) -> Unit,
) {
    ModalBottomSheetLayout(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            AddQuestionBottomSheet(
                addQuestion = {
                    addQuestion(companyId)
                    sheetScope.launch { sheetState.hide() }
                },
                question = question,
                answer = answer,
                setQuestion = setQuestion,
                setAnswer = setAnswer,
            )
        },
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = JobisTheme.colors.inverseSurface,
    ) {
        Column(
            modifier = Modifier
                .background(JobisTheme.colors.background)
                .fillMaxSize(),
        ) {
            JobisLargeTopAppBar(
                onBackPressed = onBackPressed,
                title = stringResource(id = R.string.write_review),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 4.dp,
                    )
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = JobisTheme.colors.surfaceVariant,
                    ),
            ) {
                JobisText(
                    text = stringResource(id = R.string.empty),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubBody,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
            JobisButton(
                text = stringResource(id = R.string.add_question),
                onClick = { sheetScope.launch { sheetState.show() } },
            )
        }
    }
}

@Composable
private fun AddQuestionBottomSheet(
    addQuestion: () -> Unit,
    question: String,
    answer: String,
    setQuestion: (String) -> Unit,
    setAnswer: (String) -> Unit,
) {
    Column {
        JobisText(
            text = stringResource(id = R.string.add_question),
            color = JobisTheme.colors.onSurfaceVariant,
            style = JobisTypography.SubBody,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 16.dp,
                start = 24.dp,
                end = 24.dp,
            ),
        )
        JobisTextField(
            value = { question },
            hint = "example",
            onValueChange = setQuestion,
            title = "질문",
            fieldColor = JobisTheme.colors.background,
        )
        JobisTextField(
            value = { answer },
            hint = "example",
            onValueChange = setAnswer,
            title = "답변",
            fieldColor = JobisTheme.colors.background,
        )
        JobisButton(
            text = stringResource(id = R.string.add_question),
            onClick = addQuestion,
            color = ButtonColor.Primary,
        )
    }
}
