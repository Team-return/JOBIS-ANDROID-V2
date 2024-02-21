package team.retum.jobisandroidv2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun Application(
    onBackPressed: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val urls = remember { mutableStateListOf<String>() }

    ApplicationScreen(
        scrollState = scrollState,
        onBackPressed = onBackPressed,
        urls = urls,
        onUrlChange = { index, url -> urls[index] = url },
        onAddUrlClick = { urls.add("") },

    )
}

@Composable
private fun ApplicationScreen(
    scrollState: ScrollState,
    onBackPressed: () -> Unit,
    urls: SnapshotStateList<String>,
    onUrlChange: (Int, String) -> Unit,
    onAddUrlClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
            title = "지원하기", // stringResource(id = R.string.apply),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            CompanyInformation(
                companyProfileUrl = "",
                companyName = "㈜비바리퍼블리카",
            )
            Attachments(
                attachments = listOf("길근우 -자기소개서").toMutableStateList(),
                onClick = { /*TODO*/ },
                onRemoveClick = {},
            )
            Urls(
                urls = urls,
                onUrlChange = onUrlChange,
                onClick = onAddUrlClick,
            )
        }
        JobisButton(
            text = "지원하기",
            onClick = {},
            color = ButtonColor.Primary,
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
        // TODO AsyncImage 사용
        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(8.dp),
                ),
            painter = painterResource(id = JobisIcon.Information),
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
    attachments: SnapshotStateList<String>,
    onClick: () -> Unit,
    onRemoveClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "첨부 파일",
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        if (attachments.isEmpty()) {
            AddApplicationDocument(
                text = "파일 추가하기",
                onClick = onClick,
            )
        } else {
            attachments.forEachIndexed { index, name ->
                Attachment(
                    name = name,
                    onRemoveClick = { onRemoveClick(index) },
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
    JobisCard {
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
            text = "URL",
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        urls.forEachIndexed { index, url ->
            JobisTextField(
                title = "title",
                value = { url },
                hint = "링크를 입력해주세요",
                onValueChange = { onUrlChange(index, it) },
            ) {
            }
        }
        AddApplicationDocument(
            text = "URL 추가하기",
            onClick = onClick,
        )
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
            .padding(horizontal = 24.dp)
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
