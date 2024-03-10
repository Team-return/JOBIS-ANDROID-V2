package team.retum.home.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.common.enums.ApplyStatus
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
import team.retum.usecase.entity.application.AppliedCompaniesEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import java.time.LocalDate

private const val PAGE_COUNT = 4
private const val INITIAL_PAGE = 40
private const val MAX_PAGE = 100
private const val INDICATOR_DURATION = 500
private val bannerResources = listOf<Int>()

private data class MenuItem(
    val title: String,
    val onClick: () -> Unit,
    @DrawableRes val icon: Int,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Home(
    onAlarmClick: () -> Unit,
    showRejectionModal: (String) -> Unit,
    onCompaniesClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(INITIAL_PAGE) { MAX_PAGE }
    val currentDate = LocalDate.now()
    val isDecemberOrLater = currentDate.monthValue >= 12
    val menus = if (isDecemberOrLater) {
        listOf(
            MenuItem(
                title = stringResource(id = R.string.search_other_companies),
                onClick = onCompaniesClick,
                icon = R.drawable.ic_building,
            ),
            MenuItem(
                title = stringResource(id = R.string.experiential_field_training),
                onClick = {},
                icon = R.drawable.ic_snowman,
            ),
        )
    } else {
        listOf(
            MenuItem(
                title = stringResource(id = R.string.how_about_other_companies),
                onClick = onCompaniesClick,
                icon = R.drawable.ic_building,
            ),
        )
    }

    LaunchedEffect(Unit) {
        homeViewModel.sideEffect.collect {
            when (it) {
                is HomeSideEffect.ShowRejectionModal -> {
                    showRejectionModal(it.rejectionReason)
                }
            }
        }
    }

    HomeScreen(
        pagerState = pagerState,
        menus = menus,
        onAlarmClick = onAlarmClick,
        onRejectionReasonClick = homeViewModel::onRejectionReasonClick,
        state = state,
        studentInformation = state.studentInformation,
        appliedCompanies = homeViewModel.appliedCompanies,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    pagerState: PagerState,
    menus: List<MenuItem>,
    onAlarmClick: () -> Unit,
    onRejectionReasonClick: (Long) -> Unit,
    state: HomeState,
    studentInformation: StudentInformationEntity,
    appliedCompanies: List<AppliedCompaniesEntity.ApplicationEntity>,
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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            /*Banner(
                pagerState = pagerState,
                rate = state.rate,
                passCount = state.passCount,
                totalStudentCount = state.totalStudentCount,
            )*/
            EmploymentRate(
                rate = state.rate,
                passCount = state.passCount,
                totalStudentCount = state.totalStudentCount,
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
                appliedCompanies = appliedCompanies,
                onClick = onRejectionReasonClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.Banner(
    pagerState: PagerState,
    rate: Float,
    passCount: Long,
    totalStudentCount: Long,
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 10.dp,
        ),
        pageSpacing = 12.dp,
    ) {
        when (it % PAGE_COUNT) {
            0 -> {
                EmploymentRate(
                    rate = rate,
                    passCount = passCount,
                    totalStudentCount = totalStudentCount,
                )
            }

            else -> {
                Image(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .aspectRatio(2f)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = painterResource(id = JobisIcon.Information),
                    contentDescription = "banner",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
    BannerIndicator(
        pageCount = PAGE_COUNT,
        currentPage = pagerState.currentPage,
    )
}

@Composable
private fun EmploymentRate(
    rate: Float,
    passCount: Long,
    totalStudentCount: Long,
) {
    JobisCard(
        modifier = Modifier.padding(
            top = 16.dp,
            bottom = 32.dp,
            start = 24.dp,
            end = 24.dp,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(24.dp)) {
                JobisText(
                    text = stringResource(id = R.string.employment_rate, 6),
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
                            append("현재")
                        }
                        withStyle(SpanStyle(JobisTheme.colors.inverseOnSurface)) {
                            append(" $passCount/$totalStudentCount ")
                        }
                        withStyle(SpanStyle(JobisTheme.colors.onSurfaceVariant)) {
                            append("명이 취업했어요")
                        }
                    },
                    style = JobisTypography.Description,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_file),
                contentDescription = "file",
            )
        }
    }
}

@Composable
private fun ColumnScope.BannerIndicator(
    pageCount: Int,
    currentPage: Int,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(pageCount) {
            val color by animateColorAsState(
                targetValue = if (it == currentPage % PAGE_COUNT) {
                    JobisTheme.colors.onPrimary
                } else {
                    JobisTheme.colors.surfaceVariant
                },
                label = "",
                animationSpec = tween(INDICATOR_DURATION),
            )
            val width by animateDpAsState(
                targetValue = if (it == currentPage % PAGE_COUNT) {
                    12.dp
                } else {
                    6.dp
                },
                label = "",
                animationSpec = tween(INDICATOR_DURATION),
            )

            Box(
                modifier = Modifier
                    .size(
                        height = 6.dp,
                        width = width,
                    )
                    .clip(CircleShape)
                    .background(color),
            )
        }
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
    appliedCompanies: List<AppliedCompaniesEntity.ApplicationEntity>,
    onClick: (Long) -> Unit,
) {
    Column(modifier = modifier) {
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
            Divider(
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
                    ApplyCompanyItem(
                        onClick = { onClick(it.applicationId) },
                        appliedCompany = it,
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

@Composable
private fun ApplyCompanyItem(
    onClick: () -> Unit,
    appliedCompany: AppliedCompaniesEntity.ApplicationEntity,
) {
    val applicationStatus = appliedCompany.applicationStatus
    val color = when (applicationStatus) {
        ApplyStatus.FAILED, ApplyStatus.REJECTED -> JobisTheme.colors.error
        ApplyStatus.REQUESTED, ApplyStatus.APPROVED -> JobisTheme.colors.tertiary
        ApplyStatus.FIELD_TRAIN, ApplyStatus.ACCEPTANCE, ApplyStatus.PASS -> JobisTheme.colors.outlineVariant
        else -> JobisTheme.colors.onPrimary
    }
    JobisCard(
        onClick = if (applicationStatus == ApplyStatus.REJECTED) {
            onClick
        } else {
            null
        },
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO AsyncImage 사용
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(id = JobisIcon.Information),
                contentDescription = "company profile url",
            )
            Spacer(modifier = Modifier.width(8.dp))
            JobisText(
                modifier = Modifier.weight(1f),
                text = appliedCompany.company,
                style = JobisTypography.Body,
            )
            JobisText(
                text = applicationStatus.value,
                style = JobisTypography.SubBody,
                color = color,
            )
            if (applicationStatus == ApplyStatus.REJECTED) {
                JobisText(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.reason),
                    style = JobisTypography.SubBody,
                    color = color,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    }
}
