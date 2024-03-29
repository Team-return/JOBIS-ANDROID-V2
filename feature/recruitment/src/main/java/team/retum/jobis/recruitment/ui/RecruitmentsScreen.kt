package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentsContent
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
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        with(recruitmentViewModel) {
            setJobCode(RecruitmentFilterViewModel.jobCode)
            setTechCode(RecruitmentFilterViewModel.techCode)
            recruitmentViewModel.clearRecruitment()
            recruitmentViewModel.fetchRecruitments()
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.callNextPageByPosition()
        }
    }

    RecruitmentsScreen(
        recruitments = recruitmentViewModel.getRecruitments(),
        onRecruitmentClick = onRecruitmentDetailsClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
        onBookmarkClick = recruitmentViewModel::bookmarkRecruitment,
        lazyListState = lazyListState,
    )
}

@Composable
private fun RecruitmentsScreen(
    recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    onBookmarkClick: (Long) -> Unit,
    lazyListState: LazyListState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        // TODO collapsing top app bar lazyliststate 이용하게 변경
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.recruitment),
        ) {
            JobisIconButton(
                painter = painterResource(JobisIcon.Filter),
                contentDescription = "filter",
                onClick = onRecruitmentFilterClick,
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                painter = painterResource(JobisIcon.Search),
                contentDescription = "search",
                onClick = onSearchRecruitmentClick,
            )
        }
        RecruitmentsContent(
            lazyListState = lazyListState,
            recruitments = recruitments,
            onRecruitmentClick = onRecruitmentClick,
            onBookmarkClick = onBookmarkClick,
        )
    }
}
