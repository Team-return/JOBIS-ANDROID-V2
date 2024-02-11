package team.retum.recruitment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.returm.jobisdesignsystemv2.appbar.JobisCollapsingTopAppBar
import team.returm.jobisdesignsystemv2.button.JobisIconButton
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText
import team.returm.jobisdesignsystemv2.utils.clickable
import java.text.DecimalFormat

// TODO 서버 연동 시 제거
private data class Recruitment(
    val recruitId: Long,
    val companyProfileUrl: String,
    val title: String,
    val military: Boolean,
    val trainPay: Long,
    val company: String,
    val isBookmarked: Boolean,
)

@Composable
internal fun Recruitments() {
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
    RecruitmentsScreen(recruitments = recruitments.toMutableStateList())
}

@Composable
private fun RecruitmentsScreen(
    recruitments: SnapshotStateList<Recruitment>,
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
            recruitments.forEach {
                RecruitmentContent(
                    recruitment = it,
                    onClick = { },
                )
            }
        }
    }
}

@Composable
private fun RecruitmentContent(
    recruitment: Recruitment,
    onClick: (recruitId: Long) -> Unit,
) {
    val middleText = StringBuilder().apply {
        append(stringResource(R.string.military))
        append(if (recruitment.military) " O " else " X ")
        append(" · ")
        append(stringResource(R.string.recruitment))
        append(" ")
        append(DecimalFormat().format(recruitment.trainPay))
    }.toString()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    enabled = true,
                    onClick = { onClick(recruitment.recruitId) },
                    onPressed = {},
                ),
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(JobisIcon.Information),
                contentDescription = "company profile",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                JobisText(
                    text = recruitment.title,
                    style = JobisTypography.SubHeadLine,
                )
                JobisText(
                    text = middleText,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.inverseOnSurface,
                )
                JobisText(
                    text = recruitment.company,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        JobisIconButton(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(
                id = if (recruitment.isBookmarked) {
                    JobisIcon.BookmarkOn
                } else {
                    JobisIcon.BookmarkOff
                }
            ),
            contentDescription = "bookmark",
            onClick = { },
        )
    }
}
