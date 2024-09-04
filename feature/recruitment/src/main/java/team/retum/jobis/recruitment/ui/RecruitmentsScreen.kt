package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentItems
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.usecase.entity.RecruitmentsEntity

@Composable
internal fun Recruitments(
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        with(recruitmentViewModel) {
            setJobCode(RecruitmentFilterViewModel.jobCode)
            setTechCode(RecruitmentFilterViewModel.techCode)
            clearRecruitment()
            fetchTotalRecruitmentCount()
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
    onSearchRecruitmentClick: () -> Unit,
    onBookmarkClick: (Long) -> Unit,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
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
                onClick = onSearchRecruitmentClick,
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
}
