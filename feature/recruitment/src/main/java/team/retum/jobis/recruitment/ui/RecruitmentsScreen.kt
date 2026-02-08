package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.RecruitmentsEntity
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement


@Composable
internal fun Recruitments(
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

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
        onRecruitmentClick = onRecruitmentDetailsClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
        onBookmarkClick = recruitmentViewModel::bookmarkRecruitment,
        whetherFetchNextPage = recruitmentViewModel::whetherFetchNextPage,
        fetchNextPage = recruitmentViewModel::fetchRecruitments,
    )
}

@Composable
private fun RecruitmentsScreen(
    recruitments: ImmutableList<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    onBookmarkClick: (BookmarkLocalEntity) -> Unit,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    var sortExpanded by remember {
        mutableStateOf(false)
    }

    var selectedSort by remember {
        mutableStateOf("기본순")
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
            .padding(start = 24.dp, top = 72.dp, end = 24.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.clickable {
            sortExpanded = true
        }
    )
    {
        Text(
            text = "$selectedSort ▾ ",
            maxLines = 1,
        )
        DropdownMenu(
            expanded = sortExpanded,
            onDismissRequest = { sortExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("기본순") },
                onClick = {
                    selectedSort = "기본순"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
            DropdownMenuItem(
                text = { Text("매출") },
                onClick = {
                    selectedSort = "매출"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
            DropdownMenuItem(
                text = { Text("직원 ↓") },
                onClick = {
                    selectedSort = "직원 ↓"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
            DropdownMenuItem(
                text = { Text("직원 ↑") },
                onClick = {
                    selectedSort = "직원 ↑"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
            DropdownMenuItem(
                text = { Text("공고마감 ↓") },
                onClick = {
                    selectedSort = "공고마감 ↓"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
            DropdownMenuItem(
                text = { Text("공고마감 ↑") },
                onClick = {
                    selectedSort = "공고마감 ↑"
                    sortExpanded = false
                    // TODO : viewModel에 정렬할 값 전달할 수 있게
                }
            )
        }
    }
    }

}
