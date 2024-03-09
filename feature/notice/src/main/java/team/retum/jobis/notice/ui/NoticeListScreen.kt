package team.retum.jobis.notice.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.notification.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun Notices(
    onBackPressed: () -> Unit,
) {
    NoticesScreen(
        onBackPressed = onBackPressed,
        scrollState = rememberScrollState(),
    )
}

@Composable
private fun NoticesScreen(
    onBackPressed: () -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.announcement),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
        ) {
            // TODO: viewmodel 추가시 제거
            for (i in 0..20) {
                NoticeListItem(
                    noticeTitle = "[중요] 오리엔테이션날 일정 안내",
                    noticeDate = "2024.01.17",
                    onClick = { /*TODO 공지사항 상세페이지로 이동*/ },
                )
            }
        }
    }
}

@Composable
private fun NoticeListItem(
    noticeTitle: String,
    noticeDate: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                JobisText(
                    text = noticeTitle,
                    style = JobisTypography.Body,
                    color = JobisTheme.colors.onSurface,
                )
                JobisText(
                    text = noticeDate,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onSurfaceVariant,
                )
            }
            Icon(
                painter = painterResource(id = JobisIcon.ArrowRight),
                contentDescription = "Arrow Icon",
                tint = JobisTheme.colors.surfaceTint,
            )
        }
    }
}
