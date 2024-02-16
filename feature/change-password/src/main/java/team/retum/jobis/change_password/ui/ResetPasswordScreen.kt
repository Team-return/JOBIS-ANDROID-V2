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
internal fun ResetPassword(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    ResetPasswordScreen(
        onBackPressed = onBackPressed,
        onCompleteClick = onCompleteClick,
    )
}

@Composable
private fun ResetPasswordScreen(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    // TODO 뷰모델로 옮기기
    var password by remember { mutableStateOf("") }
    var checkPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            onBackPressed = onBackPressed,
            title = stringResource(id = R.string.title_reset_password),
        )
        ResetPasswordInputs(
            password = { password },
            checkPassword = { checkPassword },
            onPasswordChange = { password = it },
            onCheckPasswordChange = { checkPassword = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.complete),
            onClick = onCompleteClick,
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun ResetPasswordInputs(
    password: () -> String,
    checkPassword: () -> String,
    onPasswordChange: (String) -> Unit,
    onCheckPasswordChange: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.password),
        hint = stringResource(id = R.string.hint_password),
        value = password,
        onValueChange = onPasswordChange,
        informationDescription = stringResource(id = R.string.information_password),
        showEmailHint = true,
        showVisibleIcon = true,
        showDescription = { true },
    )
    JobisTextField(
        title = stringResource(id = R.string.check_password),
        hint = stringResource(id = R.string.hint_check_password),
        value = checkPassword,
        onValueChange = onCheckPasswordChange,
        showVisibleIcon = true,
    )
}
