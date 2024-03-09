package team.retum.jobis.application.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.jobis.application.R
import team.retum.jobis.application.model.CompanyInfo
import team.retum.jobis.application.viewmodel.ApplicationSideEffect
import team.retum.jobis.application.viewmodel.ApplicationState
import team.retum.jobis.application.viewmodel.ApplicationViewModel
import team.retum.jobis.application.viewmodel.MAX_ATTACHMENT_COUNT
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import java.io.File

@Composable
internal fun Application(
    onBackPressed: () -> Unit,
    companyInfo: CompanyInfo,
    applicationViewModel: ApplicationViewModel = hiltViewModel(),
) {
    val state by applicationViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            applicationViewModel.addAttachment(
                activityResult = it,
                context = context,
            )
        },
    )

    LaunchedEffect(Unit) {
        applicationViewModel.sideEffect.collect {
            when (it) {
                is ApplicationSideEffect.ExceedFileCount -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_max_count),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    ApplicationScreen(
        state = state,
        scrollState = scrollState,
        onBackPressed = onBackPressed,
        urls = applicationViewModel.getUrls(),
        onUrlChange = applicationViewModel::onUrlChange,
        onAddUrlClick = applicationViewModel::addUrl,
        onRemoveUrlClick = applicationViewModel::removeUrl,
        companyInfo = companyInfo,
        onAddAttachmentClick = {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                addCategory(Intent.CATEGORY_OPENABLE)
                launcher.launch(this)
            }
        },
        removeAttachmentClick = applicationViewModel::removeAttachment,
        attachments = applicationViewModel.getAttachments(),
    )
}

@Composable
private fun ApplicationScreen(
    state: ApplicationState,
    scrollState: ScrollState,
    onBackPressed: () -> Unit,
    urls: SnapshotStateList<String>,
    onUrlChange: (Int, String) -> Unit,
    onAddUrlClick: () -> Unit,
    onRemoveUrlClick: (Int) -> Unit,
    companyInfo: CompanyInfo,
    onAddAttachmentClick: () -> Unit,
    removeAttachmentClick: (Int) -> Unit,
    attachments: SnapshotStateList<File>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
            title = stringResource(id = R.string.apply),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            CompanyInformation(
                companyProfileUrl = companyInfo.companyProfileUrl.replace(" ", "/"),
                companyName = companyInfo.companyName,
            )
            Attachments(
                attachments = attachments,
                onClick = onAddAttachmentClick,
                onRemoveClick = removeAttachmentClick,
            )
            Urls(
                urls = urls,
                onUrlChange = onUrlChange,
                onClick = onAddUrlClick,
                onRemoveUrlClick = onRemoveUrlClick,
            )
        }
        JobisButton(
            text = stringResource(id = R.string.apply),
            onClick = {},
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun CompanyInformation(
    companyProfileUrl: String,
    companyName: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(8.dp),
                ),
            model = companyProfileUrl,
            contentDescription = "company profile url",
        )
        JobisText(
            text = companyName,
            style = JobisTypography.HeadLine,
        )
    }
}

@Composable
private fun Attachments(
    attachments: SnapshotStateList<File>,
    onClick: () -> Unit,
    onRemoveClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            ),
            text = stringResource(id = R.string.attachment),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            attachments.forEachIndexed { index, file ->
                Attachment(
                    name = file.name,
                    onRemoveClick = { onRemoveClick(index) },
                )
            }
        }
        if (attachments.size < MAX_ATTACHMENT_COUNT) {
            Box(modifier = Modifier.padding(vertical = 8.dp)) {
                AddApplicationDocument(
                    text = stringResource(id = R.string.add_attachment),
                    onClick = onClick,
                )
            }
        }
    }
}

@Composable
private fun Attachment(
    name: String,
    onRemoveClick: () -> Unit,
) {
    JobisCard(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = name,
                style = JobisTypography.Body,
            )
            JobisIconButton(
                painter = painterResource(id = JobisIcon.Close),
                contentDescription = "remove",
                tint = JobisTheme.colors.onSurfaceVariant,
                onClick = onRemoveClick,
                defaultBackgroundColor = JobisTheme.colors.inverseSurface,
            )
        }
    }
}

@Composable
private fun Urls(
    urls: SnapshotStateList<String>,
    onUrlChange: (Int, String) -> Unit,
    onClick: () -> Unit,
    onRemoveUrlClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            ),
            text = stringResource(id = R.string.url),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        urls.forEachIndexed { index, url ->
            Box(contentAlignment = Alignment.CenterEnd) {
                JobisTextField(
                    value = { url },
                    hint = stringResource(id = R.string.hint_url),
                    onValueChange = { onUrlChange(index, it) },
                    leadingIcon = painterResource(id = JobisIcon.Link),
                )
                JobisIconButton(
                    modifier = Modifier.padding(end = 32.dp),
                    painter = painterResource(id = JobisIcon.Close),
                    contentDescription = "close",
                    onClick = { onRemoveUrlClick(index) },
                    defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                )
            }
        }
        if (urls.size < 3) {
            AddApplicationDocument(
                text = stringResource(id = R.string.add_url),
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun AddApplicationDocument(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                //vertical = 8.dp,
            )
            .clickable(
                enabled = true,
                onClick = onClick,
                onPressed = {},
            )
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = JobisTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = JobisIcon.Plus),
            contentDescription = "plus",
            tint = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            text = text,
            color = JobisTheme.colors.onSurfaceVariant,
            style = JobisTypography.Body,
        )
    }
}
