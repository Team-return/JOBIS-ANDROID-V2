package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentContent
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisCollapsingTopAppBar
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

    val state by recruitmentViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        recruitmentViewModel.fetchTotalRecruitmentCount(
            name = state.name,
            techCode = null,
            jobCode = null,
        )
    }

    LaunchedEffect(state.checkRecruitment) {
        if (state.checkRecruitment) {
            recruitmentViewModel.fetchRecruitments(
                page = state.page,
                name = state.name,
                jobCode = null,
                techCode = null,
            )
        }
    }

    RecruitmentsScreen(
        recruitments = recruitmentViewModel.recruitments.value.recruitments,
        onRecruitmentDetailsClick = onRecruitmentDetailsClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
        checkRecruitments = recruitmentViewModel::setCheckRecruitment,
        onBookmarked = recruitmentViewModel::bookmarkRecruitment,
        pageCount = state.page,
        totalPageCount = state.totalPage,
    )
}

@Composable
private fun RecruitmentsScreen(
    recruitments: List<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    checkRecruitments: (Boolean) -> Unit,
    onBookmarked: (Long) -> Unit,
    pageCount: Int,
    totalPageCount: Long,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisCollapsingTopAppBar(
            title = stringResource(id = R.string.recruitment),
            scrollState = scrollState,
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
        if (recruitments.isNotEmpty()) {
            LazyColumn {
                items(recruitments) { recruitment ->
                    RecruitmentContent(
                        recruitment = recruitment,
                        onClick = { onRecruitmentDetailsClick(recruitment.id) },
                        onBookmarked = onBookmarked,
                    )
                    if (recruitment == recruitments.last() && pageCount.toLong() != totalPageCount) {
                        checkRecruitments(true)
                    }
                }
            }
        } else checkRecruitments(true)
        checkRecruitments(false)
    }
}
