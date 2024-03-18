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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
        items(
            items = recruitments,
            key = { it.id },
        ) { recruitment ->
            val (bookmarked, setBookmarked) = remember { mutableStateOf(recruitment.bookmarked) }
            RecruitmentContent(
                recruitment = recruitment,
                onClick = onRecruitmentClick,
                bookmarkIcon = painterResource(
                    id = if (bookmarked) JobisIcon.BookmarkOn
                    else JobisIcon.BookmarkOff,
                ),
                onBookmarked = { setBookmarked(!bookmarked) },
            )
        }
    }
}

@Composable
private fun RecruitmentContent(
    recruitment: RecruitmentsEntity.RecruitmentEntity,
    onClick: (recruitId: Long) -> Unit,
    bookmarkIcon: Painter,
    onBookmarked: (recruitId: Long) -> Unit,
) {
    val context = LocalContext.current
    val middleText = remember {
        StringBuilder().apply {
            append(context.getString(R.string.military))
            append(if (recruitment.militarySupport) " O " else " X ")
            append(" · ")
            append(context.getString(R.string.train_pay))
            append(" ")
            append(DecimalFormat().format(recruitment.trainPay) + "만원")
        }.toString()
    }

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
                .clickable(onClick = { onClick(recruitment.id) }),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = recruitment.companyProfileUrl,
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
            painter = bookmarkIcon,
            contentDescription = "bookmark",
            onClick = { onBookmarked(recruitment.id) },
            tint = JobisTheme.colors.onPrimary,
        )
    }
}
