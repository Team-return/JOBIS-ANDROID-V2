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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.recruitment.ui.component.RecruitmentContent
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
fun SearchRecruitment(
    onBackPressed: () -> Unit,
    onRecruitmentClick: (Long) -> Unit,
) {
    SearchRecruitmentScreen(
        onBackPressed = onBackPressed,
        onRecruitmentClick = onRecruitmentClick,
    )
}

@Composable
private fun SearchRecruitmentScreen(
    onBackPressed: () -> Unit,
    onRecruitmentClick: (Long) -> Unit,
) {
    // TODO 뷰모델로 옮기기
    var keyword by remember { mutableStateOf("") }
    val recruitments = remember { mutableStateListOf<Recruitment>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        JobisTextField(
            title = "title",
            value = { keyword },
            hint = "검색어를 입력해주세요",
            onValueChange = { keyword = it },
        )
        Recruitments(
            recruitments = recruitments,
            onRecruitmentClick = onRecruitmentClick,
        )
    }
}

@Composable
private fun Recruitments(
    recruitments: SnapshotStateList<Recruitment>,
    onRecruitmentClick: (Long) -> Unit,
) {
    if (recruitments.isEmpty()) {
        EmptyRecruitmentContent()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(recruitments) { index, recruitment ->
                RecruitmentContent(
                    recruitment = recruitment,
                    onClick = { onRecruitmentClick(recruitment.recruitId) },
                )
            }
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
            painter = painterResource(id = JobisIcon.Information),
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
