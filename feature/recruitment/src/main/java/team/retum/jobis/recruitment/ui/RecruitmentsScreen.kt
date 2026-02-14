package team.retum.jobis.recruitment.ui

import team.retum.common.enums.RecruitSortType
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentItems
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentsSideEffect
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.RecruitmentsEntity

@Composable
internal fun Recruitments(
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by recruitmentViewModel.state.collectAsState()
    val currentSortType = state.sortType

    LaunchedEffect(Unit) {
        with(recruitmentViewModel) {
            setJobCode(RecruitmentFilterViewModel.jobCode)
            setTechCode(RecruitmentFilterViewModel.techCode)
            setWinterIntern(isWinterIntern = false)
            setYears(RecruitmentFilterViewModel.year)
            setStatus(RecruitmentFilterViewModel.status)
            clearRecruitment()
            fetchTotalRecruitmentCount()
        }

        recruitmentViewModel.sideEffect.collect {
            when (it) {
                is RecruitmentsSideEffect.FetchRecruitmentsError -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.cannot_find_recruitment),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    RecruitmentsScreen(
        recruitments = recruitmentViewModel.recruitments.toPersistentList(),
        currentSortType = currentSortType,
        onRecruitmentClick = onRecruitmentDetailsClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
        onBookmarkClick = recruitmentViewModel::bookmarkRecruitment,
        whetherFetchNextPage = recruitmentViewModel::whetherFetchNextPage,
        fetchNextPage = recruitmentViewModel::fetchRecruitments,
        onSortChange = { newSortType ->
            recruitmentViewModel.setSortType(newSortType)
        },
    )
}

@Composable
private fun RecruitmentsScreen(
    recruitments: ImmutableList<RecruitmentsEntity.RecruitmentEntity>,
    currentSortType: RecruitSortType?,
    onRecruitmentClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    onBookmarkClick: (BookmarkLocalEntity) -> Unit,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
    onSortChange: (RecruitSortType?) -> Unit,
) {
    var sortExpanded by remember {
        mutableStateOf(false)
    }

    val sortItems = listOf(
        "기본순",
        "매출",
        "직원 ↓",
        "직원 ↑",
        "공고마감 ↓",
        "공고마감 ↑",
    )

    val selectedSortText = when (currentSortType) {
        null -> "기본순"
        RecruitSortType.TAKE -> "매출"
        RecruitSortType.WORKERS_COUNT_ASC -> "직원 ↓"
        RecruitSortType.WORKERS_COUNT_DESC -> "직원 ↑"
        RecruitSortType.DEADLINE_ASC -> "공고마감 ↓"
        RecruitSortType.DEADLINE_DESC -> "공고마감 ↑"
        else -> "기본순"
    }

    fun getSortType(label: String): RecruitSortType? = when (label) {
        "기본순" -> null
        "매출" -> RecruitSortType.TAKE
        "직원 ↓" -> RecruitSortType.WORKERS_COUNT_ASC
        "직원 ↑" -> RecruitSortType.WORKERS_COUNT_DESC
        "공고마감 ↓" -> RecruitSortType.DEADLINE_ASC
        "공고마감 ↑" -> RecruitSortType.DEADLINE_DESC
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(title = stringResource(id = R.string.recruitment)) {
            JobisIconButton(
                drawableResId = JobisIcon.Filter,
                contentDescription = "filter",
                onClick = onRecruitmentFilterClick,
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = "search",
                onClick = { onSearchRecruitmentClick(false) },
            )
        }
        RecruitmentItems(
            recruitments = recruitments,
            onRecruitmentClick = onRecruitmentClick,
            onBookmarkClick = onBookmarkClick,
            whetherFetchNextPage = whetherFetchNextPage,
            fetchNextPage = fetchNextPage,
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, top = 70.dp, end = 24.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable {
                sortExpanded = true
            },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedSortText,
                    maxLines = 1,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onSurfaceVariant,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "정렬 펼치기",
                    tint = JobisTheme.colors.onSurfaceVariant,
                )
            }
            DropdownMenu(
                expanded = sortExpanded,
                onDismissRequest = { sortExpanded = false },
                offset = DpOffset(x = 0.dp, y = 8.dp),
                modifier = Modifier
                    .width(120.dp)
                    .align(Alignment.TopEnd)
                    .border(
                        width = 1.dp,
                        color = JobisTheme.colors.surfaceTint,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .background(JobisTheme.colors.background),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
            ) {
                sortItems.forEach { text ->
                    val isSelected = text == selectedSortText
                    DropdownMenuItem(
                        modifier = Modifier.height(36.dp),
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = text,
                                    color = if (isSelected) JobisTheme.colors.onPrimary else JobisTheme.colors.onSurfaceVariant,
                                    fontSize = 14.sp,
                                    style = JobisTypography.Description,
                                )
                            }
                        },
                        onClick = {
                            sortExpanded = false
                            onSortChange(getSortType(text))
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    )
                }
            }
        }
    }
}
