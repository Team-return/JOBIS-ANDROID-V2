package team.retum.jobis.notice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.notice.viewmodel.NoticesSideEffect
import team.retum.jobis.notice.viewmodel.NoticesViewModel
import team.retum.jobis.notification.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.notice.NoticesEntity

@Composable
internal fun Notices(
    onBackPressed: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    noticesViewModel: NoticesViewModel = hiltViewModel(),
) {
    val state = noticesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        noticesViewModel.fetchNotices()
        noticesViewModel.sideEffect.collect {
            when (it) {
                is NoticesSideEffect.BadRequest -> {}
            }
        }
    }

    NoticesScreen(
        onBackPressed = onBackPressed,
        navigateToDetail = navigateToDetail,
        notices = state.value.notices,
    )
}

@Composable
private fun NoticesScreen(
    onBackPressed: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    notices: List<NoticesEntity.NoticeEntity>,
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
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp),
        ) {
            items(notices) {
                NoticesItem(
                    noticeId = it.noticeId,
                    noticeTitle = it.title,
                    noticeDate = it.createdAt,
                    onClick = { navigateToDetail(it.noticeId) },
                )
            }
        }
    }
}

@Composable
private fun NoticesItem(
    noticeId: Long,
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
