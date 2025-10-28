package team.retum.post.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.enums.ReviewProcess
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.post.review.R
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.viewmodel.PostReviewSideEffect
import team.retum.post.review.viewmodel.PostReviewState
import team.retum.post.review.viewmodel.PostReviewViewModel
import team.retum.usecase.entity.CodesEntity

const val PAGER_COUNT = 4

/**
 * Hosts the post-review multi-step UI, collects state from the view model, and handles navigation and side effects during the review flow.
 *
 * Collects view model state, listens for side effects (success toast, navigation to the next review step with PostReviewData, and bad-request toast), triggers code fetches when the search keyword changes, and controls which bottom-sheet step is shown.
 *
 * @param onBackPressed Callback invoked when the user requests to navigate back.
 * @param companyName The display name of the company shown in the UI.
 * @param companyId The identifier for the company included in the final PostReviewData when navigating to the next review screen.
 * @param navigateToPostNextReview Callback invoked with PostReviewData when the review flow completes and navigation to the next screen should occur.
 */
@Composable
internal fun PostReview(
    onBackPressed: () -> Unit,
    companyName: String,
    companyId: Long,
    navigateToPostNextReview: (PostReviewData) -> Unit,
    reviewViewModel: PostReviewViewModel = hiltViewModel(),
) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf<ReviewProcess?>(null) }

    LaunchedEffect(Unit) {
        reviewViewModel.fetchMyReview()
        reviewViewModel.sideEffect.collect {
            when(it) {
                is PostReviewSideEffect.Success -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.post_review_success),
                    ).show()
                }

                is PostReviewSideEffect.MoveToNext -> {
                    navigateToPostNextReview(
                        PostReviewData(
                            companyId = companyId,
                            interviewType = it.interviewType,
                            location = it.location,
                            jobCode = it.jobCode,
                            interviewerCount = it.interviewerCount,
                        )
                    )
                }

                PostReviewSideEffect.BadRequest -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.post_review_bad_request),
                        drawable = JobisIcon.Error
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(state.keyword) {
        reviewViewModel.fetchCodes(state.keyword)
    }

    PostReviewScreen(
        onBackPressed = onBackPressed,
        state = state,
        onAddReviewClick = { currentStep = ReviewProcess.INTERVIEW_TYPE },
        currentStep = currentStep,
        onDismiss = { currentStep = null },
        onStepChange = { currentStep = it },
        companyName = companyName,
        setInterviewerCount = reviewViewModel::setInterviewerCount,
        setInterviewType = reviewViewModel::setInterviewType,
        setInterviewLocation = reviewViewModel::setInterviewLocation,
        setButtonClear = reviewViewModel::setButtonClear,
        setChecked = reviewViewModel::setChecked,
        setKeyword = reviewViewModel::setKeyword,
        setSelectedTech = reviewViewModel::setSelectedTech,
        techs = reviewViewModel.techs,
        buttonEnabled = state.buttonEnabled,
        onPostNextClick = reviewViewModel::onNextClick,
    )
}

/**
 * Renders the post-review screen and drives the multi-step bottom-sheet flow for creating a review.
 *
 * Displays the user's existing reviews (or an empty state) with a button to start adding a review,
 * and conditionally shows one of the review bottom sheets based on `currentStep`.
 *
 * @param state The UI state for the post-review flow (selected values, existing reviews, and UI flags).
 * @param currentStep The active review step to display as a bottom sheet, or `null` to hide sheets.
 * @param companyName The company name shown in the summary bottom sheet.
 * @param techs The observable list of available technology codes to show in the tech selection sheet.
 * @param buttonEnabled Whether the bottom-sheet "Next" button should be enabled.
 */
@Composable
private fun PostReviewScreen(
    onBackPressed: () -> Unit,
    state: PostReviewState,
    onAddReviewClick: () -> Unit,
    currentStep: ReviewProcess?,
    onDismiss: () -> Unit,
    onStepChange: (ReviewProcess?) -> Unit,
    companyName: String,
    setKeyword: (String?) -> Unit,
    setSelectedTech: (Long?) -> Unit,
    setChecked: (String?) -> Unit,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
    setInterviewerCount: (String) -> Unit,
    setInterviewType: (InterviewType) -> Unit,
    setInterviewLocation: (InterviewLocation) -> Unit,
    setButtonClear: () -> Unit,
    buttonEnabled: Boolean,
    onPostNextClick: () -> Unit,
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
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (state.myReview.isNotEmpty()) {
                items(state.myReview.size) {
                    val myReview = state.myReview[it]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .background(color = JobisTheme.colors.onPrimary, shape = RoundedCornerShape(12.dp)),
                    ) {
                        JobisText(
                            text = "${myReview.companyName} 후기 작성 완료",
                            color = JobisTheme.colors.background,
                            style = JobisTypography.HeadLine,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 16.dp,
                                    horizontal = 16.dp,
                                ),
                            maxLines = 1,
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = JobisTheme.colors.surfaceVariant,
                            ),
                    ) {
                        JobisText(
                            text = stringResource(id = R.string.empty),
                            color = JobisTheme.colors.onSurfaceVariant,
                            style = JobisTypography.HeadLine,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 16.dp,
                                    horizontal = 16.dp,
                                ),
                        )
                    }
                }
            }
            item {
                JobisButton(
                    text = stringResource(id = R.string.add_review),
                    onClick = onAddReviewClick,
                )
            }
        }
    }

    // 각 단계별로 별도의 바텀시트 표시
    when (currentStep) {
        ReviewProcess.INTERVIEW_TYPE -> {
            InterviewTypeBottomSheet(
                onDismiss = onDismiss,
                onNextClick = {
                    setButtonClear()
                    onStepChange(ReviewProcess.INTERVIEW_LOCATION)
                },
                setInterviewType = setInterviewType,
                interviewType = state.interviewType,
                buttonEnabled = buttonEnabled,
            )
        }

        ReviewProcess.INTERVIEW_LOCATION -> {
            InterviewLocationBottomSheet(
                onDismiss = onDismiss,
                onBackPressed = { onStepChange(ReviewProcess.INTERVIEW_TYPE) },
                onNextClick = {
                    setButtonClear()
                    onStepChange(ReviewProcess.TECH_STACK)
                },
                setInterviewLocation = setInterviewLocation,
                interviewLocation = state.interviewLocation,
                buttonEnabled = buttonEnabled,
            )
        }

        ReviewProcess.TECH_STACK -> {
            TechStackBottomSheet(
                onDismiss = onDismiss,
                onBackPressed = { onStepChange(ReviewProcess.INTERVIEW_LOCATION) },
                onNextClick = {
                    setButtonClear()
                    onStepChange(ReviewProcess.INTERVIEWER_COUNT)
                },
                setKeyword = setKeyword,
                setSelectedTech = setSelectedTech,
                setChecked = setChecked,
                state = state,
                techs = techs,
                buttonEnabled = buttonEnabled,
            )
        }

        ReviewProcess.INTERVIEWER_COUNT -> {
            InterviewerCountBottomSheet(
                onDismiss = onDismiss,
                onBackPressed = { onStepChange(ReviewProcess.TECH_STACK) },
                onNextClick = {
                    setButtonClear()
                    onStepChange(ReviewProcess.SUMMARY)
                },
                setInterviewerCount = setInterviewerCount,
                state = state,
                buttonEnabled = buttonEnabled,
            )
        }

        ReviewProcess.SUMMARY -> {
            SummaryBottomSheet(
                onDismiss = onDismiss,
                onBackPressed = { onStepChange(ReviewProcess.INTERVIEWER_COUNT) },
                onNextClick = onPostNextClick,
                state = state,
                companyName = companyName,
            )
        }

        null -> { /* 바텀시트 숨김 */ }
    }
}

/**
 * Modal bottom sheet that lets the user choose an interview type and continue to the next step.
 *
 * Presents three selectable options (individual, group, other); updates selection via `setInterviewType`
 * and exposes navigation controls for dismissing or advancing with `onDismiss` and `onNextClick`.
 *
 * @param onDismiss Called when the sheet is dismissed or the back button is pressed.
 * @param onNextClick Called when the Next button is pressed.
 * @param setInterviewType Callback invoked with the chosen [InterviewType].
 * @param interviewType Currently selected [InterviewType].
 * @param buttonEnabled Whether the Next button is enabled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterviewTypeBottomSheet(
    onDismiss: () -> Unit,
    onNextClick: () -> Unit,
    setInterviewType: (InterviewType) -> Unit,
    interviewType: InterviewType,
    buttonEnabled: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisIconButton(
                    drawableResId = JobisIcon.Arrow,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                    contentDescription = stringResource(id = team.retum.design_system.R.string.content_description_arrow),
                    onClick = onDismiss,
                )
                JobisText(
                    text = stringResource(id = R.string.review_category),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubHeadLine,
                )
            }
            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(PAGER_COUNT) {
                    val color = if (it == 0) JobisTheme.colors.onPrimary else JobisTheme.colors.surfaceVariant
                    val multiple = if (it == 0) 1.8f else 1f
                    Box(
                        modifier = Modifier
                            .background(color = color, shape = RoundedCornerShape(200.dp))
                            .size(width = 12.dp * multiple, height = 6.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                JobisText(
                    text = "답변",
                    color = JobisTheme.colors.onSurface,
                    style = JobisTypography.Description
                )
                Icon(
                    painter = painterResource(JobisIcon.Asterisk),
                    contentDescription = "Required",
                    tint = JobisTheme.colors.secondary,
                    modifier = Modifier.size(12.dp)
                )
            }
            Column(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PostReviewOutlinedStrokeText(
                    selected = interviewType == InterviewType.INDIVIDUAL,
                    text = stringResource(id = R.string.individual_review),
                    onButtonClick = { setInterviewType(InterviewType.INDIVIDUAL) },
                )

                PostReviewOutlinedStrokeText(
                    selected = interviewType == InterviewType.GROUP,
                    text = stringResource(id = R.string.group_review),
                    onButtonClick = { setInterviewType(InterviewType.GROUP) },
                )

                PostReviewOutlinedStrokeText(
                    selected = interviewType == InterviewType.OTHER,
                    text = stringResource(id = R.string.other_review),
                    onButtonClick = { setInterviewType(InterviewType.OTHER) },
                )
            }
            JobisButton(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                color = ButtonColor.Primary,
                enabled = buttonEnabled,
            )
        }
    }
}

/**
 * Shows a modal bottom sheet for selecting the interview location and advancing the review flow.
 *
 * The sheet displays four location options (Daejeon, Seoul, Gyeonggi, Other), highlights the
 * currently selected option, and provides Back, Dismiss, and Next controls.
 *
 * @param onDismiss Called when the sheet is dismissed (outside tap or programmatic dismissal).
 * @param onBackPressed Called when the header back button is pressed.
 * @param onNextClick Called when the Next button is pressed.
 * @param setInterviewLocation Callback to update the selected interview location.
 * @param interviewLocation The currently selected interview location.
 * @param buttonEnabled Controls whether the Next button is enabled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterviewLocationBottomSheet(
    onDismiss: () -> Unit,
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    setInterviewLocation: (InterviewLocation) -> Unit,
    interviewLocation: InterviewLocation,
    buttonEnabled: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisIconButton(
                    drawableResId = JobisIcon.Arrow,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                    contentDescription = stringResource(id = team.retum.design_system.R.string.content_description_arrow),
                    onClick = onBackPressed,
                )
                JobisText(
                    text = stringResource(id = R.string.review_location),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubHeadLine,
                )
            }
            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(PAGER_COUNT) {
                    val color = if (it == 1) JobisTheme.colors.onPrimary else JobisTheme.colors.surfaceVariant
                    val multiple = if (it == 1) 1.8f else 1f
                    Box(
                        modifier = Modifier
                            .background(color = color, shape = RoundedCornerShape(200.dp))
                            .size(width = 12.dp * multiple, height = 6.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                JobisText(
                    text = "답변",
                    color = JobisTheme.colors.onSurface,
                    style = JobisTypography.Description
                )
                Icon(
                    painter = painterResource(JobisIcon.Asterisk),
                    contentDescription = "Required",
                    tint = JobisTheme.colors.secondary,
                    modifier = Modifier.size(12.dp)
                )
            }
            Column(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PostReviewOutlinedStrokeText(
                    selected = interviewLocation == InterviewLocation.DAEJEON,
                    text = stringResource(id = R.string.deajeon),
                    onButtonClick = { setInterviewLocation(InterviewLocation.DAEJEON) },
                )
                PostReviewOutlinedStrokeText(
                    selected = interviewLocation == InterviewLocation.SEOUL,
                    text = stringResource(id = R.string.seoul),
                    onButtonClick = { setInterviewLocation(InterviewLocation.SEOUL) },
                )
                PostReviewOutlinedStrokeText(
                    selected = interviewLocation == InterviewLocation.GYEONGGI,
                    text = stringResource(id = R.string.gyeonggi),
                    onButtonClick = { setInterviewLocation(InterviewLocation.GYEONGGI) },
                )

                PostReviewOutlinedStrokeText(
                    selected = interviewLocation == InterviewLocation.OTHER,
                    text = stringResource(id = R.string.other),
                    onButtonClick = { setInterviewLocation(InterviewLocation.OTHER) },
                )
            }
            JobisButton(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                color = ButtonColor.Primary,
                enabled = buttonEnabled,
            )
        }
    }
}

/**
 * Shows a modal bottom sheet that lets the user search and select a technology from the tech stack.
 *
 * @param onDismiss Called when the sheet is dismissed.
 * @param onBackPressed Called when the sheet's back action is triggered.
 * @param onNextClick Called when the Next button is pressed.
 * @param setKeyword Updates the current search keyword (pass `null` to clear).
 * @param setSelectedTech Sets the selected technology code id (pass `null` to clear).
 * @param setChecked Sets the selected technology keyword (pass `null` to clear).
 * @param state The current post-review state containing keyword and checked values.
 * @param techs List of available technology code entities to display.
 * @param buttonEnabled Controls whether the Next button is enabled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TechStackBottomSheet(
    onDismiss: () -> Unit,
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    setKeyword: (String?) -> Unit,
    setSelectedTech: (Long?) -> Unit,
    setChecked: (String?) -> Unit,
    state: PostReviewState,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
    buttonEnabled: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisIconButton(
                    drawableResId = JobisIcon.Arrow,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                    contentDescription = stringResource(id = team.retum.design_system.R.string.content_description_arrow),
                    onClick = onBackPressed,
                )
                JobisText(
                    text = stringResource(id = R.string.support_position),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubHeadLine,
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(PAGER_COUNT) {
                    val color = if (it == 2) JobisTheme.colors.onPrimary else JobisTheme.colors.surfaceVariant
                    val multiple = if (it == 2) 1.8f else 1f
                    Box(
                        modifier = Modifier
                            .background(color = color, shape = RoundedCornerShape(200.dp))
                            .size(width = 12.dp * multiple, height = 6.dp)
                    )
                }
            }

            JobisTextField(
                value = { state.keyword ?: "" },
                hint = stringResource(id = R.string.search),
                drawableResId = JobisIcon.Search,
                onValueChange = setKeyword,
                fieldColor = JobisTheme.colors.background,
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                techs.forEach { codes ->
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                    ) {
                        JobisCheckBox(
                            checked = state.checked == codes.keyword,
                            onClick = {
                                if (state.checked == codes.keyword) {
                                    setChecked(null)
                                    setKeyword(null)
                                    setSelectedTech(null)
                                } else {
                                    setChecked(codes.keyword)
                                    setKeyword(codes.keyword)
                                    setSelectedTech(codes.code)
                                }
                            },
                            backgroundColor = JobisTheme.colors.background,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        JobisText(
                            text = codes.keyword,
                            style = JobisTypography.Body,
                            color = JobisTheme.colors.inverseOnSurface,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
            }

            JobisButton(
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                color = ButtonColor.Primary,
                enabled = buttonEnabled,
            )
        }
    }
}

/**
 * Shows a modal bottom sheet that lets the user enter the number of interviewers and proceed.
 *
 * The sheet displays a header with back/dismiss controls, a required-answer indicator, a numeric
 * text field bound to the provided state, and a Next button.
 *
 * @param onDismiss Called when the sheet is dismissed.
 * @param onBackPressed Called when the back control in the sheet header is pressed.
 * @param onNextClick Called when the Next button is pressed.
 * @param setInterviewerCount Callback to update the interviewer count; receives the new count as a string.
 * @param state Current post-review state containing the current `count` value shown in the text field.
 * @param buttonEnabled Whether the Next button is enabled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterviewerCountBottomSheet(
    onDismiss: () -> Unit,
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    setInterviewerCount: (String) -> Unit,
    state: PostReviewState,
    buttonEnabled: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisIconButton(
                    drawableResId = JobisIcon.Arrow,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                    contentDescription = stringResource(id = team.retum.design_system.R.string.content_description_arrow),
                    onClick = onBackPressed,
                )
                JobisText(
                    text = stringResource(id = R.string.interviewer_count),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubHeadLine,
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(PAGER_COUNT) {
                    val color = if (it == 3) JobisTheme.colors.onPrimary else JobisTheme.colors.surfaceVariant
                    val multiple = if (it == 3) 1.8f else 1f
                    Box(
                        modifier = Modifier
                            .background(color = color, shape = RoundedCornerShape(200.dp))
                            .size(width = 12.dp * multiple, height = 6.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                JobisText(
                    text = "답변",
                    color = JobisTheme.colors.onSurface,
                    style = JobisTypography.Description
                )
                Icon(
                    painter = painterResource(JobisIcon.Asterisk),
                    contentDescription = "Required",
                    tint = JobisTheme.colors.secondary,
                    modifier = Modifier.size(12.dp)
                )
            }

            JobisTextField(
                value = { state.count },
                hint = stringResource(id = R.string.search),
                onValueChange = setInterviewerCount,
                fieldColor = JobisTheme.colors.background,
                keyboardType = KeyboardType.Number,
            )

            JobisButton(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                color = ButtonColor.Primary,
                enabled = buttonEnabled,
            )
        }
    }
}

/**
 * Displays a modal summary bottom sheet showing the selected review details and a Next action.
 *
 * Shows human-readable interview type and location, company name (with trailing "..." removed), selected job keyword, and interviewer count. Provides back, dismiss, and next controls.
 *
 * @param onDismiss Called when the sheet requests dismissal (outside tap or drag).
 * @param onBackPressed Called when the header back button is pressed.
 * @param onNextClick Called when the Next button is pressed to proceed with submission/navigation.
 * @param state Current PostReviewState containing interviewType, interviewLocation, keyword, and count.
 * @param companyName Company name string displayed in the summary (trailing ellipsis may be present).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SummaryBottomSheet(
    onDismiss: () -> Unit,
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: PostReviewState,
    companyName: String,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val interviewType = when (state.interviewType) {
        InterviewType.INDIVIDUAL -> "개인 면접"
        InterviewType.GROUP -> "단체 면접"
        InterviewType.OTHER -> "기타 면접"
    }
    val interviewLocation = when (state.interviewLocation) {
        InterviewLocation.DAEJEON -> "대전"
        InterviewLocation.SEOUL -> "서울"
        InterviewLocation.GYEONGGI -> "경기"
        InterviewLocation.OTHER -> "기타"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dragHandle = null,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 12.dp,
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisIconButton(
                    drawableResId = JobisIcon.Arrow,
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                    contentDescription = stringResource(id = team.retum.design_system.R.string.content_description_arrow),
                    onClick = onBackPressed,
                )
                JobisText(
                    text = stringResource(id = R.string.review_category),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubHeadLine,
                )
            }

            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TopBottomText(
                    topText = "면접 구분",
                    bottomText = interviewType,
                )
                TopBottomText(
                    topText = "면접 지역",
                    bottomText = interviewLocation
                )
                TopBottomText(
                    topText = "업체명",
                    bottomText = companyName.removeSuffix("..."),
                )
                TopBottomText(
                    topText = "지원 직무",
                    bottomText = state.keyword ?: "",
                )
                TopBottomText(
                    topText = "면접관 수",
                    bottomText = state.count,
                )
            }

            JobisButton(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                color = ButtonColor.Primary,
                enabled = true,
            )
        }
    }
}

/**
 * Displays a vertical label-and-value pair with a smaller top label and a prominent bottom value.
 *
 * @param topText The top label text.
 * @param bottomText The bottom value text shown prominently below the label.
 */
@Composable
private fun TopBottomText(
    topText: String,
    bottomText: String,
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        JobisText(
            text = topText,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            text = bottomText,
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onPrimary
        )
    }
}

/**
 * Renders a selectable, pill-styled outlined text row used as an option in the post-review flow.
 *
 * @param selected Whether the option is selected; controls the item's visual appearance.
 * @param text The label displayed inside the outlined pill.
 * @param onButtonClick Callback invoked when the item is clicked.
 */
@Composable
private fun PostReviewOutlinedStrokeText(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    onButtonClick: () -> Unit,
) {
    val borderColor = if (selected) {
        JobisTheme.colors.onPrimary
    } else {
        JobisTheme.colors.surfaceVariant
    }
    val textColor = if (selected) {
        JobisTheme.colors.onPrimary
    } else {
        JobisTheme.colors.onSurfaceVariant
    }

    JobisText(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp,
            )
            .clickable(
                onClick = onButtonClick,
            ),
        text = text,
        color = textColor,
        style = JobisTypography.SubHeadLine,
        textAlign = TextAlign.Center,
    )
}