package team.retum.bug.ui

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import team.retum.bug.viewmodel.ReportBugSideEffect
import team.retum.bug.viewmodel.ReportBugState
import team.retum.bug.viewmodel.ReportBugViewModel
import team.retum.common.enums.DevelopmentArea
import team.retum.jobis.bug.R
import team.retum.jobisdesignsystemv2.appbar.JobisCollapsingTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable

private val developmentAreas = listOf(
    DevelopmentArea.ANDROID,
    DevelopmentArea.IOS,
    DevelopmentArea.WEB,
    DevelopmentArea.SERVER,
    DevelopmentArea.ALL,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ReportBug(
    onBackPressed: () -> Unit,
    reportBugViewModel: ReportBugViewModel = hiltViewModel(),
) {
    val state by reportBugViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val screenshots = reportBugViewModel.getUris()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = reportBugViewModel::addUris,
    )

    LaunchedEffect(Unit) {
        reportBugViewModel.sideEffect.collect {
            when (it) {
                is ReportBugSideEffect.MaxScreenshotCount -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.screenshot_over_flow),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is ReportBugSideEffect.Success -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_report_bug_success),
                    ).show()
                    onBackPressed()
                }
            }
        }
    }

    ReportBugScreen(
        onBackPressed = onBackPressed,
        state = state,
        onTitleChange = reportBugViewModel::setTitle,
        onContentChange = reportBugViewModel::setContent,
        onReportBugClick = { reportBugViewModel.onNextClick(context = context) },
        activityResultLauncher = activityResultLauncher,
        screenshots = screenshots.toMutableStateList(),
        onRemoveClick = reportBugViewModel::removeScreenshot,
        onFieldSelect = {
            reportBugViewModel.setDevelopmentArea(developmentArea = it)
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        scrollState = scrollState,
        sheetState = sheetState,
        coroutineScope = coroutineScope,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReportBugScreen(
    onBackPressed: () -> Unit,
    state: ReportBugState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onReportBugClick: () -> Unit,
    activityResultLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    screenshots: SnapshotStateList<Uri>,
    onRemoveClick: (Int) -> Unit,
    onFieldSelect: (DevelopmentArea) -> Unit,
    scrollState: ScrollState,
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {
    ModalBottomSheetLayout(
        sheetContent = {
            FieldBottomSheet(onFieldSelect = onFieldSelect)
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JobisTheme.colors.background),
        ) {
            JobisCollapsingTopAppBar(
                title = stringResource(id = R.string.report_bug),
                onBackPressed = onBackPressed,
                scrollState = scrollState,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                FieldDropDown(
                    selectedField = state.developmentArea,
                    onClick = {
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    },
                )
                ReportBugInputs(
                    title = { state.title },
                    content = { state.content },
                    onTitleChange = onTitleChange,
                    onContentChange = onContentChange,
                )
                Screenshots(
                    screenshots = screenshots,
                    onClick = { activityResultLauncher.launch(PickVisualMediaRequest()) },
                    onRemoveClick = onRemoveClick,
                )
            }
            JobisButton(
                text = stringResource(id = R.string.report_bug),
                onClick = onReportBugClick,
                color = ButtonColor.Primary,
                enabled = state.buttonEnabled,
            )
        }
    }
}

@Composable
private fun FieldBottomSheet(
    onFieldSelect: (DevelopmentArea) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JobisTheme.colors.background)
            .padding(top = 12.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            ),
            text = stringResource(id = R.string.select_field),
            style = JobisTypography.SubBody,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        developmentAreas.forEach { developmentArea ->
            var pressed by remember { mutableStateOf(false) }
            val backgroundColor by animateColorAsState(
                targetValue = if (pressed) {
                    JobisTheme.colors.inverseSurface
                } else {
                    JobisTheme.colors.background
                },
                label = "",
            )

            JobisText(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = true,
                        onPressed = { pressed = it },
                        onClick = { onFieldSelect(developmentArea) },
                    )
                    .background(backgroundColor)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 12.dp,
                    ),
                text = developmentArea.value,
                style = JobisTypography.Body,
                color = JobisTheme.colors.onSurface,
            )
        }
    }
}

@Composable
private fun FieldDropDown(
    selectedField: DevelopmentArea,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            vertical = 12.dp,
            horizontal = 24.dp,
        ),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.field),
            style = JobisTypography.Description,
            color = JobisTheme.colors.inverseOnSurface,
        )
        JobisCard(
            modifier = Modifier.padding(vertical = 4.dp),
            onClick = onClick,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                JobisText(
                    text = selectedField.value,
                    style = JobisTypography.SubHeadLine,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "arrow",
                    tint = JobisTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ReportBugInputs(
    title: () -> String,
    content: () -> String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.title),
        value = title,
        hint = stringResource(id = R.string.hint_title),
        onValueChange = onTitleChange,
    )
    JobisTextField(
        title = stringResource(id = R.string.content),
        value = content,
        hint = stringResource(id = R.string.hint_content),
        onValueChange = onContentChange,
        singleLine = false,
        imeAction = ImeAction.Default,
    )
}

@Composable
private fun Screenshots(
    screenshots: SnapshotStateList<Uri>,
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
            text = stringResource(id = R.string.screenshots),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        if (screenshots.isEmpty()) {
            AddImage(
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 4.dp,
                ),
                onClick = onClick,
                text = stringResource(id = R.string.attachment_screenshots),
            )
        } else {
            LazyRow(
                modifier = Modifier.padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(screenshots) { index, uri ->
                    Screenshot(
                        uri = uri,
                        index = index,
                        onRemoveClick = onRemoveClick,
                    )
                }
                if (screenshots.size < 5) {
                    item {
                        AddImage(
                            modifier = Modifier.size(92.dp),
                            onClick = onClick,
                            text = stringResource(id = R.string.add_image),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = JobisTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = "add photo",
            tint = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            text = text,
            style = JobisTypography.SubBody,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun Screenshot(
    uri: Uri,
    index: Int,
    onRemoveClick: (Int) -> Unit,
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        AsyncImage(
            modifier = Modifier
                .size(92.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = JobisTheme.colors.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                ),
            model = uri,
            contentDescription = "bug image",
            contentScale = ContentScale.Crop,
        )
        DeleteButton(
            index = index,
            onClick = onRemoveClick,
        )
    }
}

@Composable
private fun DeleteButton(
    index: Int,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp,
            )
            .clickable(onClick = { onClick(index) })
            .clip(CircleShape)
            .background(JobisTheme.colors.error)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = JobisIcon.Close),
            contentDescription = "close",
            tint = JobisTheme.colors.background,
        )
        JobisText(
            text = stringResource(id = R.string.remove),
            style = JobisTypography.Caption,
            color = JobisTheme.colors.background,
        )
    }
}
