package team.retum.review.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.retum.common.enums.ReviewProcess
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.review.viewmodel.ReviewSideEffect
import team.retum.review.viewmodel.ReviewState
import team.retum.review.viewmodel.ReviewViewModel
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.PostReviewEntity

const val SEARCH_DELAY: Long = 200

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
    val pagerState = rememberPagerState(pageCount = { 5 })
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

    LaunchedEffect(state.keyword) {
        delay(SEARCH_DELAY)
        reviewViewModel.fetchCodes(state.keyword)
    }

    PostReviewScreen(
        onBackPressed = onBackPressed,
        sheetScope = sheetScope,
        hideModalBottomSheet = { sheetScope.launch { sheetState.hide() } },
        onSheetShow = { sheetScope.launch { sheetState.show() } },
        sheetState = sheetState,
        pagerState = pagerState,
        addQuestion = {
            reviewViewModel.addReview()
            reviewViewModel.keywords.add(state.keyword)
        },
        state = state,
        setQuestion = reviewViewModel::setQuestion,
        setAnswer = reviewViewModel::setAnswer,
        setKeyword = reviewViewModel::setKeyword,
        setSelectedTech = reviewViewModel::setSelectedTech,
        setChecked = reviewViewModel::setChecked,
        onReviewProcessChange = reviewViewModel::setReviewProcess,
        reviewProcess = state.reviewProcess,
        techs = reviewViewModel.techs,
        fetchQuestion = { reviewViewModel.postReview(companyId) },
        reviews = reviewViewModel.reviews,
        setInit = reviewViewModel::setInit,
        keywords = reviewViewModel.keywords,
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PostReviewScreen(
    onBackPressed: () -> Unit,
    sheetScope: CoroutineScope,
    hideModalBottomSheet: () -> Unit,
    onSheetShow: () -> Unit,
    sheetState: ModalBottomSheetState,
    pagerState: PagerState,
    addQuestion: () -> Unit,
    state: ReviewState,
    setQuestion: (String) -> Unit,
    setAnswer: (String) -> Unit,
    setKeyword: (String) -> Unit,
    setSelectedTech: (Long) -> Unit,
    setChecked: (String) -> Unit,
    onReviewProcessChange: (ReviewProcess) -> Unit,
    reviewProcess: ReviewProcess,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
    fetchQuestion: () -> Unit,
    reviews: SnapshotStateList<PostReviewEntity.PostReviewContentEntity>,
    setInit: () -> Unit,
    keywords: SnapshotStateList<String>,
) {
    ModalBottomSheetLayout(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            if (reviewProcess == ReviewProcess.FINISH) {
                hideModalBottomSheet()
                addQuestion()
            } else {
                AddQuestionBottomSheet(
                    onReviewProcess = { onReviewProcessChange(it) },
                    state = state,
                    pagerState = pagerState,
                    setQuestion = setQuestion,
                    setAnswer = setAnswer,
                    setKeyword = setKeyword,
                    setSelectedTech = setSelectedTech,
                    reviewProcess = reviewProcess,
                    setChecked = setChecked,
                    techs = techs,
                )
            }
        },
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
        ),
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
            if (reviews.isEmpty()) {
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
            } else {
                LazyColumn {
                    items(reviews.size) {
                        ReviewContent(
                            review = reviews[it],
                            keyword = keywords[it],
                        )
                    }
                }
            }
            JobisButton(
                text = stringResource(id = R.string.add_question),
                onClick = {
                    onReviewProcessChange(ReviewProcess.QUESTION)
                    onSheetShow()
                    //setInit()
                },
            )
        }
    }
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

@Composable
private fun AddQuestionBottomSheet(
    onReviewProcess: (ReviewProcess) -> Unit,
    reviewProcess: ReviewProcess,
    state: ReviewState,
    pagerState: PagerState,
    setQuestion: (String) -> Unit,
    setAnswer: (String) -> Unit,
    setKeyword: (String) -> Unit,
    setSelectedTech: (Long) -> Unit,
    setChecked: (String) -> Unit,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
    ) { page ->
        when(page) {
            0 -> {
                // TODO :: 함수로 구분해서 분리
                // 면접 구분(개인, 단체, 기타 면접)
                InterviewCategory(
                    onBackPressed = {},
                    pagerTotalCount = pagerState.pageCount,
                    currentPager = page
                )
            }
            1 -> {
                // 면접 지역(대전, 서울, 경기, 기타)
            }
            2 -> {
                // 기업명 :: 기업명 조회 api 사용
            }
            3 -> {
                // 지원 직무 :: 전공만 조회
            }
            4 -> {
                // 면접관 수 :: 숫자만 입력
            }
            5 -> {
                // 면접 환경 입력한 부분 확인 페이지
            }
            else -> {
                // TODO : 모달 입력 완료 했을 때 key 값으로 모달 초기화
                Column {
                    JobisText(
                        text = if (reviewProcess == ReviewProcess.QUESTION) {
                            stringResource(id = R.string.add_question)
                        } else {
                            stringResource(id = R.string.tech)
                        },
                        color = JobisTheme.colors.onSurfaceVariant,
                        style = JobisTypography.SubBody,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 16.dp,
                            start = 24.dp,
                            end = 24.dp,
                        ),
                    )
                    if (reviewProcess == ReviewProcess.QUESTION) {
                        JobisTextField(
                            value = { state.question },
                            hint = stringResource(id = R.string.example),
                            onValueChange = setQuestion,
                            title = stringResource(id = R.string.question),
                            fieldColor = JobisTheme.colors.background,
                        )
                        JobisTextField(
                            value = { state.answer },
                            hint = stringResource(id = R.string.example),
                            onValueChange = setAnswer,
                            title = stringResource(id = R.string.answer),
                            fieldColor = JobisTheme.colors.background,
                        )
                    } else {
                        JobisTextField(
                            value = { state.keyword },
                            hint = stringResource(id = R.string.search),
                            drawableResId = JobisIcon.Search,
                            onValueChange = setKeyword,
                            fieldColor = JobisTheme.colors.background,
                        )
                        LazyColumn(modifier = Modifier.fillMaxHeight(0.3f)) {
                            items(techs) { codes ->
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 12.dp,
                                    ),
                                ) {
                                    JobisCheckBox(
                                        checked = state.checked == codes.keyword,
                                        onClick = {
                                            setChecked(codes.keyword)
                                            setKeyword(codes.keyword)
                                            setSelectedTech(codes.code)
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
                    }
                    JobisButton(
                        text = stringResource(id = R.string.next),
                        onClick = {
                            onReviewProcess(
                                when (reviewProcess) {
                                    ReviewProcess.QUESTION -> ReviewProcess.TECH
                                    else -> ReviewProcess.FINISH
                                },
                            )
                        },
                        color = ButtonColor.Primary,
                        enabled = state.buttonEnabled,
                    )
                }
            }
        }
    }
}

@Composable
private fun InterviewCategory(
    onBackPressed: () -> Unit,
    pagerTotalCount: Int,
    currentPager: Int
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
                Box(
                    modifier = Modifier
                        .background(color = color, shape = RoundedCornerShape(200.dp))
                        .size(width = 12.dp, height = 6.dp)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                borderColor = JobisTheme.colors.surfaceVariant,
                textColor = JobisTheme.colors.onSurfaceVariant,
                text = stringResource(id = R.string.individual_review),
                onButtonClick = {}
            )
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                borderColor = JobisTheme.colors.surfaceVariant,
                textColor = JobisTheme.colors.onSurfaceVariant,
                text = stringResource(id = R.string.group_review),
                onButtonClick = {}
            )
            PostReviewOutlinedStrokeText(
                modifier = Modifier.padding(horizontal = 24.dp),
                borderColor = JobisTheme.colors.surfaceVariant,
                textColor = JobisTheme.colors.onSurfaceVariant,
                text = stringResource(id = R.string.other_review),
                onButtonClick = {}
            )
        }
        JobisButton(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(id = R.string.next),
            onClick = {},
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun PostReviewOutlinedStrokeText(
    modifier: Modifier = Modifier,
    borderColor: Color,
    textColor: Color,
    text: String,
    onButtonClick: () -> Unit,
) {
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
