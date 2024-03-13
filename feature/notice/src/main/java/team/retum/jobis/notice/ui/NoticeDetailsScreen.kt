package team.retum.jobis.notice.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.notice.viewmodel.NoticeDetailsSideEffect
import team.retum.jobis.notice.viewmodel.NoticeDetailsState
import team.retum.jobis.notice.viewmodel.NoticeDetailsViewModel
import team.retum.jobis.notification.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.notice.NoticeDetailsEntity

@Composable
internal fun NoticeDetails(
    noticeId: Long,
    onBackPressed: () -> Unit,
    noticeDetailsViewModel: NoticeDetailsViewModel = hiltViewModel(),
) {
    val state by noticeDetailsViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        noticeDetailsViewModel.fetchNoticeDetails(noticeId = noticeId)
        noticeDetailsViewModel.sideEffect.collect {
            when (it) {
                is NoticeDetailsSideEffect.BadRequest -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.occurred_error),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    NoticeDetailsScreen(
        onBackPressed = onBackPressed,
        scrollState = rememberScrollState(),
        state = state,
    )
}

@Composable
private fun NoticeDetailsScreen(
    onBackPressed: () -> Unit,
    scrollState: ScrollState,
    state: NoticeDetailsState,
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
            Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
        ) {
            Notice(noticeDetailsEntity = state.noticeDetailsEntity)
            state.noticeDetailsEntity.attachments.forEach {
                // TODO: AttachFile 함수 호출
            }
        }
    }
}

@Composable
private fun Notice(
    noticeDetailsEntity: NoticeDetailsEntity,
) {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        JobisText(
            text = noticeDetailsEntity.title,
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onSurface,
        )
        JobisText(
            modifier = Modifier.padding(top = 4.dp),
            text = noticeDetailsEntity.createdAt,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            modifier = Modifier.padding(top = 16.dp),
            text = noticeDetailsEntity.content,
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurface,
        )
    }
}

@Composable
private fun AttachFile(
    fileName: String,
) {
    Column {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.attachFile),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                modifier = Modifier.padding(start = 12.dp),
                text = fileName,
                style = JobisTypography.Body,
            )
            JobisIconButton(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = "download",
                onClick = {},
            )
        }
    }
}
