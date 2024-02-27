package team.retum.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.signup.R
import team.retum.signup.model.SignUpData

@Composable
internal fun SetPassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    signUpData: SignUpData,
) {
    SetPasswordScreen(
        onBackPressed = onBackPressed,
        onNextClick = onNextClick,
    )
}

@Composable
private fun SetPasswordScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    // TODO: viewModel로 옮기기
    var password by remember { mutableStateOf("") }
    var checkPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.setting_password),
            onBackPressed = onBackPressed,
        )
        PasswordInputs(
            password = { password },
            checkPassword = { checkPassword },
            onPasswordChange = { password = it },
            onCheckPassword = { checkPassword = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.next),
            color = ButtonColor.Primary,
            onClick = onNextClick,
        )
    }
}

@Composable
private fun PasswordInputs(
    password: () -> String,
    checkPassword: () -> String,
    onPasswordChange: (String) -> Unit,
    onCheckPassword: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.password),
        value = password,
        onValueChange = onPasswordChange,
        hint = stringResource(id = R.string.hint_password),
        showDescription = { true },
        informationDescription = "8 ~ 16자, 영문자, 숫자, 특수문자 포함",
        descriptionType = DescriptionType.Information,
    )
    JobisTextField(
        title = stringResource(id = R.string.check_password),
        value = checkPassword,
        onValueChange = onCheckPassword,
        hint = stringResource(id = R.string.hint_check_password),
    )
}
