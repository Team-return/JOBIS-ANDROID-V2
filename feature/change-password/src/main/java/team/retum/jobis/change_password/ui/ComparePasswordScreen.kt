package team.retum.jobis.change_password.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import team.retum.jobis.change_password.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun ComparePassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    ComparePasswordScreen(
        onBackPressed = onBackPressed,
        onNextClick = onNextClick,
    )
}

@Composable
private fun ComparePasswordScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    // TODO 뷰모델로 옮기기
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.title_confirm_password),
            onBackPressed = onBackPressed,
        )
        JobisTextField(
            title = stringResource(id = R.string.password),
            value = { password },
            onValueChange = { password = it },
            hint = stringResource(id = R.string.hint_password_confirm),
            showVisibleIcon = true,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.next),
            onClick = onNextClick,
            color = ButtonColor.Primary,
        )
    }
}
