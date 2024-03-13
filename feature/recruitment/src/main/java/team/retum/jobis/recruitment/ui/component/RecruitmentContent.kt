package team.retum.jobis.recruitment.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import team.retum.common.utils.ResourceKeys.IMAGE_URL
import team.retum.jobis.recruitment.R
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.RecruitmentsEntity
import java.text.DecimalFormat

@Composable
internal fun RecruitmentsContent(
    lazyListState: LazyListState,
    recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit,
) {
    LazyColumn(state = lazyListState) {
        items(recruitments) { recruitment ->
            RecruitmentContent(
                recruitment = recruitment,
                onClick = onRecruitmentClick,
                onBookmarked = onBookmarkClick,
            )
        }
    }
}

@Composable
private fun RecruitmentContent(
    recruitment: RecruitmentsEntity.RecruitmentEntity,
    onClick: (recruitId: Long) -> Unit,
    onBookmarked: (recruitId: Long) -> Unit,
) {
    val middleText = StringBuilder().apply {
        append(stringResource(R.string.military))
        append(if (recruitment.militarySupport) " O " else " X ")
        append(" · ")
        append(stringResource(R.string.train_pay))
        append(" ")
        append(DecimalFormat().format(recruitment.trainPay) + "만원")
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
                    onClick = { onClick(recruitment.id) },
                    onPressed = {},
                ),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = IMAGE_URL + recruitment.companyProfileUrl,
                contentDescription = "company profile",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                JobisText(
                    text = recruitment.companyName,
                    style = JobisTypography.SubHeadLine,
                )
                JobisText(
                    text = middleText,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        JobisIconButton(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(
                id = if (recruitment.bookmarked) {
                    JobisIcon.BookmarkOn
                } else {
                    JobisIcon.BookmarkOff
                },
            ),
            contentDescription = "bookmark",
            onClick = { onBookmarked(recruitment.id) },
            tint = JobisTheme.colors.onPrimary,
        )
    }
}
