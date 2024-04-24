package team.retum.jobis.notice.ui

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.FileProvider
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
import team.retum.jobisdesignsystemv2.popup.JobisPopup
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.notice.NoticeDetailsEntity
import java.io.File
import java.util.Locale

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

                is NoticeDetailsSideEffect.DownloadFailed -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.failed_download),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is NoticeDetailsSideEffect.DownLoadSuccess -> {
                    JobisPopup.showPopup(
                        context = context,
                        message = context.getString(R.string.success_download),
                        drawable = JobisIcon.DownLoad,
                        onClick = {
                            openDownloadedFile(
                                filePath = state.filePath,
                                context = context,
                            )
                        },
                        buttonText = context.getString(R.string.open),
                    )
                }
            }
        }
    }

    NoticeDetailsScreen(
        onBackPressed = onBackPressed,
        scrollState = rememberScrollState(),
        state = state,
        saveFileData = { url, fileName ->
            noticeDetailsViewModel.saveFileData(
                urlString = url,
                destinationPath = fileName,
                context = context,
            )
        },
    )
}

@Composable
private fun NoticeDetailsScreen(
    onBackPressed: () -> Unit,
    scrollState: ScrollState,
    state: NoticeDetailsState,
    saveFileData: (String, String) -> Unit,
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
            Notice(noticeDetailsEntity = state.noticeDetailsEntity)
            state.noticeDetailsEntity.attachments.forEach {
                AttachFile(
                    fileName = it.url,
                    saveFileData = saveFileData,
                )
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
    saveFileData: (url: String, fileName: String) -> Unit,
) {
    Column {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.attachFile),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = JobisTheme.colors.inverseSurface,
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 14.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                modifier = Modifier.weight(1f),
                text = fileName.split("-").last(),
                style = JobisTypography.Body,
            )
            JobisIconButton(
                painter = painterResource(id = JobisIcon.DownLoad),
                contentDescription = "download",
                onClick = {
                    saveFileData(
                        fileName,
                        fileName
                            .split("-")
                            .last(),
                    )
                },
            )
        }
    }
}

private fun openDownloadedFile(
    filePath: String,
    context: Context,
) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file,
    )
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(uri, getMimeType(filePath))
    context.startActivity(intent)
}

private fun getMimeType(filePath: String): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
    return MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(extension.toLowerCase(Locale.getDefault()))
}
