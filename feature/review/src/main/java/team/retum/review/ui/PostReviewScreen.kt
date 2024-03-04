package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostReviewScreen(onBackPressed: () -> Unit) {
    val sheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val sheetScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxSize(),
        sheetState = sheetState,
        sheetContent = { AddQuestionBottomSheet() },
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = JobisTheme.colors.inverseSurface,
    ) {
        Column(
            modifier = Modifier
                .background(JobisTheme.colors.background)
                .fillMaxSize()
        ) {
            JobisLargeTopAppBar(
                onBackPressed = onBackPressed,
                title = stringResource(id = R.string.write_review),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 4.dp,
                    )
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = JobisTheme.colors.surfaceVariant,
                    ),
            ) {
                JobisText(
                    text = stringResource(id = R.string.empty),
                    color = JobisTheme.colors.onSurfaceVariant,
                    style = JobisTypography.SubBody,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
            JobisButton(
                text = stringResource(id = R.string.add_question),
                onClick = { sheetScope.launch { sheetState.show() } },
            )
        }
    }
}


@Composable
fun AddQuestionBottomSheet() {
    Column {
        JobisText(
            text = stringResource(id = R.string.add_question),
            color = JobisTheme.colors.onSurfaceVariant,
            style = JobisTypography.SubBody,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 16.dp,
                start = 24.dp,
                end = 24.dp,
            )
        )
        JobisTextField(
            value = { "" },
            hint = "example",
            onValueChange = {},
            title = "질문",
            fieldColor = JobisTheme.colors.background,
        )
        JobisTextField(
            value = { "" },
            hint = "example",
            onValueChange = {},
            title = "답변",
            fieldColor = JobisTheme.colors.background,
        )
        JobisButton(
            text = stringResource(id = R.string.add_question),
            onClick = {},
            color = ButtonColor.Primary,
        )
    }
}