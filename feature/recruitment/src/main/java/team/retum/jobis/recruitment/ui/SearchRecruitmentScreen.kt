package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentsContent
import team.retum.jobis.recruitment.viewmodel.SearchRecruitmentState
import team.retum.jobis.recruitment.viewmodel.SearchRecruitmentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.RecruitmentsEntity

@Composable
internal fun SearchRecruitment(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    searchRecruitmentViewModel: SearchRecruitmentViewModel = hiltViewModel(),
) {
    val state by searchRecruitmentViewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        with(searchRecruitmentViewModel) {
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.callNextPageByPosition()
            observeName()
        }
    }

    SearchRecruitmentScreen(
        recruitments = searchRecruitmentViewModel.recruitments,
        onBackPressed = onBackPressed,
        onRecruitmentClick = onRecruitmentDetailsClick,
        onBookmarkClick = searchRecruitmentViewModel::bookmarkRecruitment,
        state = state,
        lazyListState = lazyListState,
        onNameChange = searchRecruitmentViewModel::setName,
    )
}

@Composable
private fun SearchRecruitmentScreen(
    recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity>,
    onBackPressed: () -> Unit,
    onRecruitmentClick: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    state: SearchRecruitmentState,
    lazyListState: LazyListState,
    onNameChange: (String) -> Unit,
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
            leadingIcon = painterResource(id = JobisIcon.Search),
        )
        if (recruitments.isNotEmpty() || state.name.isNullOrEmpty()) {
            RecruitmentsContent(
                lazyListState = lazyListState,
                recruitments = recruitments,
                onRecruitmentClick = onRecruitmentClick,
                onBookmarkClick = onBookmarkClick,
            )
        } else {
            EmptyRecruitmentContent()
        }
    }
}

@Composable
private fun EmptyRecruitmentContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.ic_empty_company),
            contentDescription = "empty recruitment",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            JobisText(
                text = stringResource(id = R.string.recruitment_no_content),
                style = JobisTypography.HeadLine,
            )
            JobisText(
                text = stringResource(id = R.string.retry),
                style = JobisTypography.Body,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
