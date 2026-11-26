package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentItems
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentsSideEffect
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.empty.EmptyContent
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.RecruitmentsEntity

private enum class WinterInternEmptyType {
    SEASON_ENDED,
    NO_RECRUITMENTS,
    FILTERED,
}

@Composable
internal fun WinterIntern(
    isWinterInternAvailable: Boolean,
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by recruitmentViewModel.state.collectAsStateWithLifecycle()
    val selectedJobCode = RecruitmentFilterViewModel.jobCode
    val selectedTechCode = RecruitmentFilterViewModel.techCode?.takeIf { it.isNotBlank() }
    val selectedYears = RecruitmentFilterViewModel.year
    val selectedStatus = RecruitmentFilterViewModel.status
    val selectedRegion = RecruitmentFilterViewModel.region
    val hasRecruitments = recruitmentViewModel.recruitments.any { it.id != 0L }
    val hasActiveFilters = selectedJobCode != null ||
        selectedTechCode != null ||
        !selectedYears.isNullOrEmpty() ||
        selectedStatus != null ||
        selectedRegion != null
    val emptyType = when {
        !isWinterInternAvailable -> WinterInternEmptyType.SEASON_ENDED
        state.showRecruitmentsEmptyContent && hasActiveFilters -> WinterInternEmptyType.FILTERED
        state.showRecruitmentsEmptyContent -> WinterInternEmptyType.NO_RECRUITMENTS
        else -> null
    }

    LaunchedEffect(
        isWinterInternAvailable,
        selectedJobCode,
        selectedTechCode,
        selectedYears,
        selectedStatus,
        selectedRegion,
    ) {
        with(recruitmentViewModel) {
            val filtersChanged = setFilters(
                jobCode = selectedJobCode,
                techCode = selectedTechCode,
                years = selectedYears,
                status = selectedStatus,
                region = selectedRegion,
                isWinterIntern = true,
            )
            if (isWinterInternAvailable && (filtersChanged || !hasRecruitments)) {
                fetchTotalRecruitmentCount()
            }
        }
    }

    LaunchedEffect(Unit) {
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

    WinterInternScreen(
        onBackPressed = onBackPressed,
        emptyType = emptyType,
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
    emptyType: WinterInternEmptyType?,
    recruitments: ImmutableList<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (isWinterIntern: Boolean) -> Unit,
    onBookmarkClick: (BookmarkLocalEntity) -> Unit,
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
                onClick = { onSearchRecruitmentClick(true) },
            )
        }
        if (emptyType != null) {
            val title = when (emptyType) {
                WinterInternEmptyType.SEASON_ENDED -> R.string.winter_intern_empty_title
                WinterInternEmptyType.NO_RECRUITMENTS -> R.string.winter_intern_no_recruitments_title
                WinterInternEmptyType.FILTERED -> R.string.winter_intern_filtered_empty_title
            }
            val description = when (emptyType) {
                WinterInternEmptyType.SEASON_ENDED -> R.string.winter_intern_empty_description
                WinterInternEmptyType.NO_RECRUITMENTS -> R.string.winter_intern_no_recruitments_description
                WinterInternEmptyType.FILTERED -> R.string.winter_intern_filtered_empty_description
            }
            EmptyContent(
                title = stringResource(id = title),
                description = stringResource(id = description),
            )
        } else {
            RecruitmentItems(
                recruitments = recruitments,
                onRecruitmentClick = onRecruitmentClick,
                onBookmarkClick = onBookmarkClick,
                whetherFetchNextPage = whetherFetchNextPage,
                fetchNextPage = fetchNextPage,
            )
        }
    }
}
