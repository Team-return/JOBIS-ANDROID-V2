package team.retum.review.ui.review_write

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.jobis.review.R
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
import team.retum.review.model.PostReviewData
import team.retum.review.viewmodel.PostReviewSideEffect
import team.retum.review.viewmodel.PostReviewState
import team.retum.review.viewmodel.PostReviewViewModel
import team.retum.usecase.entity.CodesEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostReview(
    onBackPressed: () -> Unit,
    companyName: String,
    companyId: Long,
    navigateToPostNextReview: (PostReviewData) -> Unit,
    reviewViewModel: PostReviewViewModel = hiltViewModel(),
) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val sheetScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 5 })
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        reviewViewModel.setInit(companyId = companyId, companyName = companyName)
        reviewViewModel.fetchMyReview()
        reviewViewModel.sideEffect.collect {
            when(it) {
                is PostReviewSideEffect.Success -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.added_question),
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
                        message = context.getString(R.string.review_bad_request),
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
        hideModalBottomSheet = { sheetScope.launch { sheetState.hide() } },
        onSheetShow = { sheetScope.launch { sheetState.show() } },
        sheetState = sheetState,
        pagerState = pagerState,
        state = state,
        companyName = reviewViewModel.companyName.toString(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostReviewScreen(
    onBackPressed: () -> Unit,
    hideModalBottomSheet: () -> Unit,
    onSheetShow: () -> Unit,
    sheetState: SheetState,
    pagerState: PagerState,
    state: PostReviewState,
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
    onPostNextClick: () -> Unit
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
        if (state.myReview.isNotEmpty()) {
            state.myReview.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = JobisTheme.colors.onPrimary,
                        ),
                ) {
                    JobisText(
                        text = it.companyName,
                        color = JobisTheme.colors.background,
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
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
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
        JobisButton(
            text = stringResource(id = R.string.add_review),
            onClick = {
                onSheetShow()
            },
        )
    }
    ModalBottomSheet(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxSize(),
        containerColor = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
        ),
        onDismissRequest = hideModalBottomSheet,
    ) {
        AddQuestionBottomSheet(
            state = state,
            companyName = companyName,
            pagerState = pagerState,
            sheetState = sheetState,
            setInterviewType = setInterviewType,
            setInterviewLocation = setInterviewLocation,
            setInterviewerCount = setInterviewerCount,
            setButtonClear = setButtonClear,
            setKeyword = setKeyword,
            setSelectedTech = setSelectedTech,
            setChecked = setChecked,
            techs = techs,
            buttonEnabled = buttonEnabled,
            onNextClick = onPostNextClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddQuestionBottomSheet(
    state: PostReviewState,
    companyName: String,
    pagerState: PagerState,
    sheetState: SheetState,
    setKeyword: (String?) -> Unit,
    setSelectedTech: (Long?) -> Unit,
    setChecked: (String?) -> Unit,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
    setInterviewType: (InterviewType) -> Unit,
    setInterviewLocation: (InterviewLocation) -> Unit,
    setInterviewerCount: (String) -> Unit,
    setButtonClear: () -> Unit,
    onNextClick: () -> Unit,
    buttonEnabled: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val totalPage = pagerState.pageCount - 1

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                0 -> {
                    // 면접 구분(개인, 단체, 기타 면접)
                    InterviewTypeSelector(
                        onBackPressed = {
                            coroutineScope.launch {
                                setButtonClear()
                                sheetState.hide()
                            }
                        },
                        pagerTotalCount = totalPage,
                        currentPager = page,
                        onClick = { setInterviewType(it) },
                        onNextClick = {
                            setButtonClear()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        interviewType = state.interviewType,
                        buttonEnabled = buttonEnabled,
                    )
                }

                1 -> {
                    // 면접 지역(대전, 서울, 경기, 기타)
                    InterviewLocationSelector(
                        onBackPressed = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
                        pagerTotalCount = totalPage,
                        currentPager = page,
                        onClick = { setInterviewLocation(it) },
                        onNextClick = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        interviewLocation = state.interviewLocation,
                        buttonEnabled = buttonEnabled,
                    )
                }

                2 -> {
                    // 지원 직무 :: 전공만 조회
                    ApplyMajorSelector(
                        onBackPressed = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
                        setKeyword = setKeyword,
                        setChecked = setChecked,
                        setSelectedTech = setSelectedTech,
                        pagerTotalCount = totalPage,
                        currentPager = page,
                        onNextClick = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        state = state,
                        techs = techs,
                    )
                }
                3 -> {
                    // 면접관 수 :: 숫자만 입력
                    InterviewerCountSelector(
                        onBackPressed = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
                        setInterviewerCount = setInterviewerCount,
                        pagerTotalCount = totalPage,
                        currentPager = page,
                        onNextClick = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        state = state,
                        buttonEnabled = buttonEnabled,
                    )
                }
                4 -> {
                    InterviewSummary(
                        onBackPressed = {
                            coroutineScope.launch {
                                setButtonClear()
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
                        state = state,
                        companyName = companyName,
                        onNextClick = onNextClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewTypeSelector(
    onBackPressed: () -> Unit,
    pagerTotalCount: Int,
    currentPager: Int,
    onNextClick: () -> Unit,
    onClick: (InterviewType) -> Unit,
    interviewType: InterviewType,
    buttonEnabled: Boolean,
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
                modifier = Modifier,
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
        Row(
            modifier = Modifier.padding(top = 10.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(pagerTotalCount) {
                val color = if (currentPager == it)
                    JobisTheme.colors.onPrimary
                else
                    JobisTheme.colors.surfaceVariant
                val multiple = if (currentPager == it)
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
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewType == InterviewType.INDIVIDUAL,
                text = stringResource(id = R.string.individual_review),
            ) { onClick(InterviewType.INDIVIDUAL) }
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewType == InterviewType.GROUP,
                text = stringResource(id = R.string.group_review)
            ) { onClick(InterviewType.GROUP) }
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewType == InterviewType.OTHER,
                text = stringResource(id = R.string.other_review)
            ) { onClick(InterviewType.OTHER) }
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

@Composable
private fun InterviewLocationSelector(
    onBackPressed: () -> Unit,
    pagerTotalCount: Int,
    currentPager: Int,
    onNextClick: () -> Unit,
    onClick: (InterviewLocation) -> Unit,
    interviewLocation: InterviewLocation,
    buttonEnabled: Boolean,
) {
    Column(
        modifier = Modifier.padding(
            top = 24.dp,
            bottom = 12.dp,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisIconButton(
                modifier = Modifier,
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
            repeat(pagerTotalCount) {
                val color = if (currentPager == it)
                    JobisTheme.colors.onPrimary
                else
                    JobisTheme.colors.surfaceVariant
                val multiple = if (currentPager == it)
                    1.8f
                else
                    1f
                Box(
                    modifier = Modifier
                        .background(color = color, shape = RoundedCornerShape(200.dp))
                        .size(width = 12.dp * multiple, height = 6.dp),
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewLocation == InterviewLocation.DAEJEON,
                text = stringResource(id = R.string.deajeon),
            ) { onClick(InterviewLocation.DAEJEON) }
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewLocation == InterviewLocation.SEOUL,
                text = stringResource(id = R.string.seoul)
            ) { onClick(InterviewLocation.SEOUL) }
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewLocation == InterviewLocation.GYEONGGI,
                text = stringResource(id = R.string.gyeonggi)
            ) { onClick(InterviewLocation.GYEONGGI) }
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                selected = interviewLocation == InterviewLocation.OTHER,
                text = stringResource(id = R.string.other)
            ) { onClick(InterviewLocation.OTHER) }
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

@Composable
private fun ApplyMajorSelector(
    onBackPressed: () -> Unit,
    setKeyword: (String?) -> Unit,
    setSelectedTech: (Long?) -> Unit,
    setChecked: (String?) -> Unit,
    state: PostReviewState,
    pagerTotalCount: Int,
    currentPager: Int,
    onNextClick: () -> Unit,
    techs: List<CodesEntity.CodeEntity>,
) {
    Column(
        modifier = Modifier
            .heightIn(max = 600.dp)
            .padding(
                top = 24.dp,
                bottom = 12.dp,
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisIconButton(
                modifier = Modifier,
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
            repeat(pagerTotalCount) {
                val color = if (currentPager == it)
                    JobisTheme.colors.onPrimary
                else
                    JobisTheme.colors.surfaceVariant
                val multiple = if (currentPager == it)
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
        JobisTextField(
            value = { state.keyword!! },
            hint = stringResource(id = R.string.search),
            drawableResId = JobisIcon.Search,
            onValueChange = setKeyword,
            fieldColor = JobisTheme.colors.background,
        )
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(techs.size) {index ->
                val codes = techs[index]
                Row(
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                    ),
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
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun InterviewerCountSelector(
    onBackPressed: () -> Unit,
    setInterviewerCount: (String) -> Unit,
    state: PostReviewState,
    pagerTotalCount: Int,
    currentPager: Int,
    onNextClick: () -> Unit,
    buttonEnabled: Boolean,
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
                modifier = Modifier,
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
            repeat(pagerTotalCount) {
                val color = if (currentPager == it)
                    JobisTheme.colors.onPrimary
                else
                    JobisTheme.colors.surfaceVariant
                val multiple = if (currentPager == it)
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

@Composable
private fun InterviewSummary(
    onBackPressed: () -> Unit,
    state: PostReviewState,
    companyName: String,
    onNextClick: () -> Unit,
) {
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
                modifier = Modifier,
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
                bottomText = state.keyword!!,
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
