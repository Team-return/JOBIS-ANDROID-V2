package team.retum.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.textfield.DescriptionType
import team.returm.jobisdesignsystemv2.textfield.JobisTextField

@Composable
fun SettingPasswordScreen(
    onBackClick: () -> Unit,
) {
    // TODO: viewModel로 옮기기
    var password by remember { mutableStateOf("") }
    var checkPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.setting_password),
            onBackPressed = onBackClick,
        )
        PasswordInputs(
            password = { password },
            checkPassword = { checkPassword },
            onPasswordChange = { password = it },
            onCheckPassword = { checkPassword = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(vertical = 12.dp),
            text = stringResource(id = R.string.next),
            color = ButtonColor.Primary,
            onClick = { },
        )
    }
}

@Composable
fun PasswordInputs(
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
        showDescription = {true},
        informationDescription = "8 ~ 16자, 영문자, 숫자, 특수문자 포함",
        descriptionType = DescriptionType.Information
    )
    JobisTextField(
        title = stringResource(id = R.string.check_password),
        value = checkPassword,
        onValueChange = onCheckPassword,
        hint = stringResource(id = R.string.hint_check_password),
    )
}