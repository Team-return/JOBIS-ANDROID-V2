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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import team.retum.home.R
import team.returm.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.returm.jobisdesignsystemv2.button.JobisIconButton
import team.returm.jobisdesignsystemv2.card.JobisCard
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText

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

// TODO 서버 연동 시 제거
private data class ApplyCompany(
    val companyId: Long,
    val companyProfileUrl: String,
    val name: String,
    val status: String,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(onAlarmClick: () -> Unit) {
    val pagerState = rememberPagerState(INITIAL_PAGE) { MAX_PAGE }
    val menus = listOf(
        MenuItem(
            title = stringResource(id = R.string.search_other_companies),
            onClick = {},
            icon = R.drawable.ic_building,
        ),
        MenuItem(
            title = stringResource(id = R.string.experiential_field_training),
            onClick = {},
            icon = R.drawable.ic_snowman,
        ),
    )

    HomeScreen(
        pagerState = pagerState,
        menus = menus,
        onAlarmClick = onAlarmClick,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    pagerState: PagerState,
    menus: List<MenuItem>,
    onAlarmClick: () -> Unit,
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
            Banner(pagerState = pagerState)
            StudentInformation(
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                ),
                profileImageUrl = "",
                number = "3125",
                name = "박시원",
                department = "소프트웨어개발과",
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
                applyCompanies = emptyList(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.Banner(pagerState: PagerState) {
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
                EmploymentRate(rate = 76.4)
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
    rate: Double,
) {
    JobisCard(
        onClick = {},
        enabled = false,
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
                            append(" 14/64 ")
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
        // TODO AsyncImage 사용
        Image(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            painter = painterResource(id = JobisIcon.Information),
            contentDescription = "user profile image",
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
    applyCompanies: List<ApplyCompany>,
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
            if (applyCompanies.isNotEmpty()) {
                applyCompanies.forEach {
                    ApplyCompanyItem(
                        applyCompany = it,
                        onClick = { },
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
    onClick: (companyId: Long) -> Unit,
    applyCompany: ApplyCompany,
) {
    JobisCard(onClick = { onClick(applyCompany.companyId) }) {
        Row(
            modifier = Modifier.padding(12.dp),
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
                text = applyCompany.name,
                style = JobisTypography.Body,
            )
            JobisText(
                text = applyCompany.status,
                style = JobisTypography.SubBody,
            )
        }
    }
}
