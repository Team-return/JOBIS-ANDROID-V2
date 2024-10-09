package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentItems
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentsSideEffect
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.RecruitmentsEntity

@Composable
internal fun WinterIntern(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(recruitmentViewModel) {
            setJobCode(RecruitmentFilterViewModel.jobCode)
            setTechCode(RecruitmentFilterViewModel.techCode)
            setWinterIntern(isWinterIntern = true)
            clearRecruitment()
            fetchTotalRecruitmentCount()
        }
    }

    LaunchedEffect(recruitmentViewModel.sideEffect) {
        recruitmentViewModel.sideEffect.collect {
            when (it) {
                is RecruitmentsSideEffect.FetchRecruitmentsError -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.occurred_error),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    WinterInternScreen(
        onBackPressed = onBackPressed,
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
private fun WinterInternScreen(
    onBackPressed: () -> Unit,
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
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.recruitment_experiential_field_training),
            onBackPressed = onBackPressed,
        ) {
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
