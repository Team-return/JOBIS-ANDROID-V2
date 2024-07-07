package team.retum.home.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.retum.common.model.ApplicationData
import team.retum.home.R
import team.retum.home.viewmodel.HomeSideEffect
import team.retum.home.viewmodel.HomeState
import team.retum.home.viewmodel.HomeViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.application.AppliedCompaniesEntity
import team.retum.usecase.entity.banner.BannersEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import java.net.URLEncoder
import java.time.LocalDate

private data class MenuItem(
    val title: String,
    val onClick: () -> Unit,
    @DrawableRes val icon: Int,
)

private const val PAGER_AUTO_SCROLL_TIME = 3000L

@Composable
internal fun Home(
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    showRejectionModal: (ApplicationData) -> Unit,
    onCompaniesClick: () -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
    navigateToApplication: (ApplicationData) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val menus = mutableListOf(
        MenuItem(
            title = stringResource(id = R.string.how_about_other_companies),
            onClick = onCompaniesClick,
            icon = JobisIcon.Building,
        ),
    )

    LaunchedEffect(Unit) {
        with(homeViewModel) {
            calculateTerm()
            fetchStudentInformation()
            fetchAppliedCompanies()
            fetchEmploymentCount()
            fetchBanners()
        }
        if (isDecemberOrLater()) {
            menus.add(
                MenuItem(
                    title = context.getString(R.string.experiential_field_training),
                    onClick = {},
                    icon = JobisIcon.SnowMan,
                ),
            )
        }
        homeViewModel.sideEffect.collect {
            when (it) {
                is HomeSideEffect.ShowRejectionModal -> {
                    showRejectionModal(it.applicationData)
                }

                is HomeSideEffect.NotFoundApplication -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_not_found_application),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is HomeSideEffect.ScrollToApplication -> {
                    scrollState.animateScrollTo(it.sectionOneCoordinates)
                }
            }
        }
    }

    HomeScreen(
        scrollState = scrollState,
        menus = menus,
        onAlarmClick = onAlarmClick,
        onRejectionReasonClick = homeViewModel::onRejectionReasonClick,
        state = state,
        banners = state.banners,
        studentInformation = state.studentInformation,
        appliedCompanies = homeViewModel.appliedCompanies,
        applicationId = applicationId,
        setScroll = { position ->
            homeViewModel.fetchScroll(
                applicationId = applicationId,
                position = position,
                enabled = navigatedFromNotifications,
            )
        },
        navigateToRecruitmentDetails = navigateToRecruitmentDetails,
        navigateToApplication = navigateToApplication,
    )
}

private fun isDecemberOrLater(): Boolean {
    return LocalDate.now().monthValue >= 12
}

@Composable
private fun HomeScreen(
    scrollState: ScrollState,
    menus: List<MenuItem>,
    onAlarmClick: () -> Unit,
    onRejectionReasonClick: (ApplicationData) -> Unit,
    state: HomeState,
    banners: List<BannersEntity.BannerEntity>,
    studentInformation: StudentInformationEntity,
    appliedCompanies: List<AppliedCompaniesEntity.ApplicationEntity>,
    applicationId: Long?,
    setScroll: (Float) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigateToApplication: (ApplicationData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        verticalArrangement = Arrangement.Top,
    ) {
        JobisSmallTopAppBar(showLogo = true) {
            JobisIconButton(
                painter = painterResource(JobisIcon.Bell),
                contentDescription = "notification",
                onClick = onAlarmClick,
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState),
        ) {
            Banner(
                state = state,
                banners = banners,
            )

            StudentInformation(
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                ),
                profileImageUrl = studentInformation.profileImageUrl,
                number = studentInformation.studentGcn,
                name = studentInformation.studentName,
                department = studentInformation.department.value,
            )
            Menus(
                modifier = Modifier.padding(
                    vertical = 12.dp,
                    horizontal = 24.dp,
                ),
                menus = menus,
            )
            ApplyStatus(
                modifier = Modifier.padding(
                    vertical = 12.dp,
                    horizontal = 24.dp,
                ),
                applicationId = applicationId,
                appliedCompanies = appliedCompanies,
                onShowRejectionReasonClick = onRejectionReasonClick,
                setScroll = setScroll,
                navigateToRecruitmentDetails = navigateToRecruitmentDetails,
                navigateToApplication = navigateToApplication,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Banner(
    state: HomeState,
    banners: List<BannersEntity.BannerEntity>,
) {
    val pagerState = rememberPagerState { banners.size + 1 }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.settledPage) {
        launch {
            delay(PAGER_AUTO_SCROLL_TIME)
            with(pagerState) {
                val target = if (settledPage < pageCount - 1) settledPage + 1 else 0

                coroutineScope.launch {
                    animateScrollToPage(
                        page = target,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing,
                        ),
                    )
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 18.dp),
        beyondBoundsPageCount = pagerState.pageCount + 1,
    ) { page ->
        JobisCard(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 10.dp,
                    start = 6.dp,
                    end = 6.dp,
                ),
        ) {
            if (page == 0) {
                EmploymentRate(
                    term = state.term,
                    rate = state.rate,
                    passCount = state.passCount,
                    totalStudentCount = state.totalStudentCount,
                )
            } else {
                AsyncImage(
                    model = banners.getOrNull(page - 1)?.bannerUrl,
                    contentDescription = "banner",
                )
            }
        }
    }
    if (banners.isNotEmpty()) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { page ->
                val (color, size) = when (pagerState.currentPage) {
                    page -> JobisTheme.colors.onPrimary to DpSize(12.dp, 6.dp)
                    else -> JobisTheme.colors.surfaceVariant to DpSize(6.dp, 6.dp)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size),
                )
            }
        }
    }
}

@Composable
private fun EmploymentRate(
    term: Int,
    rate: String,
    passCount: Long,
    totalStudentCount: Long,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.padding(24.dp)) {
            JobisText(
                text = stringResource(id = R.string.employment_rate, term),
                style = JobisTypography.HeadLine,
            )
            JobisText(
                text = "$rate%",
                style = JobisTypography.PageTitle,
                color = JobisTheme.colors.onPrimary,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(JobisTheme.colors.onSurfaceVariant)) {
                        append(stringResource(id = R.string.today))
                    }
                    withStyle(SpanStyle(JobisTheme.colors.inverseOnSurface)) {
                        append(" $passCount/$totalStudentCount ")
                    }
                    withStyle(SpanStyle(JobisTheme.colors.onSurfaceVariant)) {
                        append(stringResource(id = R.string.get_a_job))
                    }
                },
                style = JobisTypography.Description,
            )
        }
        Image(
            painter = painterResource(id = JobisIcon.File),
            contentDescription = "file",
        )
    }
}

@Composable
private fun StudentInformation(
    modifier: Modifier = Modifier,
    profileImageUrl: String,
    number: String,
    name: String,
    department: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            model = profileImageUrl,
            contentDescription = "user profile image",
            contentScale = ContentScale.Crop,
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            JobisText(
                text = "$number $name",
                style = JobisTypography.SubHeadLine,
            )
            JobisText(
                text = department,
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
    }
}

@Composable
private fun Menus(
    modifier: Modifier = Modifier,
    menus: List<MenuItem>,
) {
    Column(modifier = modifier) {
        JobisText(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.Start),
            text = stringResource(id = R.string.search_information),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            menus.forEach {
                Menu(
                    modifier = Modifier.weight(1f),
                    text = it.title,
                    onClick = it.onClick,
                    icon = it.icon,
                )
            }
        }
    }
}

@Composable
private fun Menu(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    icon: Int,
) {
    JobisCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            JobisText(
                text = "$text ->",
                style = JobisTypography.HeadLine,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(JobisTheme.colors.background)
                    .padding(12.dp)
                    .align(Alignment.End),
            ) {
                Image(
                    modifier = Modifier.background(JobisTheme.colors.background),
                    painter = painterResource(id = icon),
                    contentDescription = "menu icon",
                )
            }
        }
    }
}

@Composable
private fun ApplyStatus(
    modifier: Modifier = Modifier,
    applicationId: Long?,
    appliedCompanies: List<AppliedCompaniesEntity.ApplicationEntity>,
    onShowRejectionReasonClick: (ApplicationData) -> Unit,
    setScroll: (position: Float) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigateToApplication: (ApplicationData) -> Unit,
) {
    Column(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                setScroll(layoutCoordinates.positionInRoot().y)
            },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = stringResource(id = R.string.apply_status),
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = JobisTheme.colors.surfaceTint,
            )
            JobisText(
                text = stringResource(id = R.string.description_apply_status),
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (appliedCompanies.isNotEmpty()) {
                appliedCompanies.forEach {
                    val applicationData = ApplicationData(
                        applicationId = it.applicationId,
                        recruitmentId = it.recruitmentId,
                        rejectionReason = "",
                        companyLogoUrl = URLEncoder.encode(it.companyLogoUrl, "UTF8"),
                        companyName = it.company,
                        isReApply = true,
                    )
                    ApplyCompanyItem(
                        onShowRejectionReasonClick = { onShowRejectionReasonClick(applicationData) },
                        appliedCompany = it,
                        isFocus = applicationId == it.applicationId,
                        navigateToRecruitmentDetails = navigateToRecruitmentDetails,
                        navigateToApplication = { navigateToApplication(applicationData) },
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = JobisTheme.colors.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    JobisText(
                        modifier = Modifier.padding(vertical = 22.dp),
                        text = stringResource(id = R.string.empty_apply_companies),
                        style = JobisTypography.Body,
                        color = JobisTheme.colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
