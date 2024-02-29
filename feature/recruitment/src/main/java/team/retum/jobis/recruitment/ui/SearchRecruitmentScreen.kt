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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentContent
import team.retum.jobis.recruitment.viewmodel.RecruitmentState
import team.retum.jobis.recruitment.viewmodel.RecruitmentViewModel
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
    recruitmentViewModel: RecruitmentViewModel = hiltViewModel(),
) {
    val state by recruitmentViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.name) {
        if (state.checkRecruitment && state.name?.isNotBlank() ?: "".isNotBlank()) {
            recruitmentViewModel.fetchRecruitments(
                page = state.page,
                name = state.name,
                jobCode = null,
                techCode = null,
            )
            recruitmentViewModel.fetchTotalRecruitmentCount(
                name = state.name,
                techCode = null,
                jobCode = null,
            )
        }
    }
    SearchRecruitmentScreen(
        onBackPressed = onBackPressed,
        onRecruitmentDetailsClick = onRecruitmentDetailsClick,
        recruitments = recruitmentViewModel.recruitments.value.recruitments,
        checkRecruitments = recruitmentViewModel::setCheckRecruitment,
        state = state,
        onNameChange = recruitmentViewModel::setName,
        onBookmarked = recruitmentViewModel::bookmarkRecruitment,
    )
}

@Composable
private fun SearchRecruitmentScreen(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    recruitments: List<RecruitmentsEntity.RecruitmentEntity>,
    checkRecruitments: (Boolean) -> Unit,
    state: RecruitmentState,
    onNameChange: (String) -> Unit,
    onBookmarked: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        JobisTextField(
            value = { state.name ?: "" },
            hint = "검색어를 입력해주세요",
            onValueChange = onNameChange,
            leadingIcon = painterResource(id = JobisIcon.Search),
        )
        Recruitments(
            recruitments = recruitments,
            onRecruitmentClick = onRecruitmentDetailsClick,
            checkRecruitments = checkRecruitments,
            onBookmarked = onBookmarked,
            state = state,
        )
    }
}

@Composable
private fun Recruitments(
    recruitments: List<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    checkRecruitments: (Boolean) -> Unit,
    onBookmarked: (Long) -> Unit,
    state: RecruitmentState,
) {
    checkRecruitments(false)
    if (recruitments.isNotEmpty() || state.name.isNullOrEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(recruitments) { _, recruitment ->
                RecruitmentContent(
                    recruitment = recruitment,
                    onClick = { onRecruitmentClick(recruitment.id) },
                    onBookmarked = onBookmarked,
                )
                if (recruitment == recruitments.last() && state.page.toLong() != state.totalPage) {
                    checkRecruitments(true)
                }
            }
        }
    } else {
        checkRecruitments(true)
        EmptyRecruitmentContent()
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
                text = "검색어와 관련된 모집의뢰서를 못 찾았어요",
                style = JobisTypography.HeadLine,
            )
            JobisText(
                text = "제대로 입력했는지 다시 한 번 확인해주세요",
                style = JobisTypography.Body,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
