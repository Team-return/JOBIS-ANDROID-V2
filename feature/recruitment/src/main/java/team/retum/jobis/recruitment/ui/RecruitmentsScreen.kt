package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.component.RecruitmentContent
import team.retum.jobisdesignsystemv2.appbar.JobisCollapsingTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

// TODO 서버 연동 시 제거
data class Recruitment(
    val recruitId: Long,
    val companyProfileUrl: String,
    val title: String,
    val military: Boolean,
    val trainPay: Long,
    val company: String,
    val isBookmarked: Boolean,
)

@Composable
internal fun Recruitments(
    onRecruitmentDetailsClick: (Long) -> Unit,
) {
    // TODO 서버 연동 시 제거
    val recruitments = listOf(
        Recruitment(
            recruitId = 0L,
            companyProfileUrl = "",
            title = "title",
            military = false,
            trainPay = 0L,
            company = "company",
            isBookmarked = true,
        ),
        Recruitment(
            recruitId = 0L,
            companyProfileUrl = "",
            title = "title",
            military = true,
            trainPay = 0L,
            company = "company",
            isBookmarked = false,
        ),
    )
    RecruitmentsScreen(
        recruitments = recruitments.toMutableStateList(),
        navigateToRecruitmentDetails = onRecruitmentDetailsClick,
    )
}

@Composable
private fun RecruitmentsScreen(
    recruitments: SnapshotStateList<Recruitment>,
    navigateToRecruitmentDetails: (Long) -> Unit,
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
                onClick = {},
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                painter = painterResource(JobisIcon.Search),
                contentDescription = "search",
                onClick = {},
            )
        }
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            recruitments.forEach { recruitment ->
                RecruitmentContent(
                    recruitment = recruitment,
                    onClick = { navigateToRecruitmentDetails(recruitment.recruitId) },
                )
            }
        }
    }
}
