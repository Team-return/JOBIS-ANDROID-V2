package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentItems
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentsState
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.empty.EmptyContent
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.RecruitmentsEntity

@Composable
internal fun SearchRecruitment(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    isWinterIntern: Boolean,
    recruitmentsViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val state by recruitmentsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        recruitmentsViewModel.setWinterIntern(isWinterIntern = isWinterIntern)
    }

    SearchRecruitmentScreen(
        recruitments = recruitmentsViewModel.recruitments.toPersistentList(),
        onBackPressed = onBackPressed,
        onRecruitmentClick = onRecruitmentDetailsClick,
        onBookmarkClick = recruitmentsViewModel::bookmarkRecruitment,
        state = state,
        onNameChange = recruitmentsViewModel::setName,
        whetherFetchNextPage = recruitmentsViewModel::whetherFetchNextPage,
        fetchNextPage = recruitmentsViewModel::fetchRecruitments,
    )
}

@Composable
private fun SearchRecruitmentScreen(
    recruitments: ImmutableList<RecruitmentsEntity.RecruitmentEntity>,
    onBackPressed: () -> Unit,
    onRecruitmentClick: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    state: RecruitmentsState,
    onNameChange: (String) -> Unit,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        JobisTextField(
            value = { state.name ?: "" },
            hint = stringResource(id = R.string.recruitment_search_hint),
            onValueChange = onNameChange,
            drawableResId = JobisIcon.Search,
        )
        RecruitmentItems(
            recruitments = recruitments,
            onRecruitmentClick = onRecruitmentClick,
            onBookmarkClick = onBookmarkClick,
            whetherFetchNextPage = whetherFetchNextPage,
            fetchNextPage = fetchNextPage,
        )
        if (state.showRecruitmentsEmptyContent) {
            EmptyContent(
                title = stringResource(id = R.string.recruitment_no_content),
                description = stringResource(id = R.string.retry),
            )
        }
    }
}
