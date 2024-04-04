package team.retum.company.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun CompanyItem(
    modifier: Modifier = Modifier,
    onCompanyContentClick: (Long) -> Unit,
    companyImageUrl: String,
    companyName: String,
    id: Long,
    take: Float,
    hasRecruitment: Boolean,
) {
    val hasRecruitmentText =
        stringResource(id = if (hasRecruitment) R.string.has_recruitment else R.string.has_not_recruitment)
    val hasRecruitmentColor =
        if (hasRecruitment) JobisTheme.colors.primaryContainer else JobisTheme.colors.onSurface
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = { onCompanyContentClick(id) }),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = companyImageUrl,
            modifier = Modifier.size(48.dp),
            contentDescription = "company image",
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            JobisText(
                text = companyName,
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.inverseOnSurface,
            )
            JobisText(
                text = hasRecruitmentText,
                style = JobisTypography.SubBody,
                color = hasRecruitmentColor,
            )
            JobisText(
                text = "연매출 ${take}억",
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
    }
}
