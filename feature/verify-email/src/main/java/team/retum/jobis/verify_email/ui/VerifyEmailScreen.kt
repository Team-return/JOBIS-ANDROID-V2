package team.retum.jobis.verify_email.ui

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
import team.retum.jobis.verify_email.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisSmallButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun VerifyEmail(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    VerifyEmailScreen(
        onBackPressed = onBackPressed,
        onNextClick = onNextClick,
        onAuthorizeClick = {},
    )
}

@Composable
private fun VerifyEmailScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    onAuthorizeClick: () -> Unit,
) {
    // TODO 뷰모델로 옮기기
    var email by remember { mutableStateOf("") }
    var authenticationCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.title_change_password),
            onBackPressed = onBackPressed,
        )
        ChangePasswordInputs(
            email = { email },
            authenticationCode = { authenticationCode },
            onEmailChange = { email = it },
            onAuthenticationCodeChange = { authenticationCode = it },
            onAuthorizeClick = {},
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.next),
            onClick = onNextClick,
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun ChangePasswordInputs(
    email: () -> String,
    authenticationCode: () -> String,
    onEmailChange: (String) -> Unit,
    onAuthenticationCodeChange: (String) -> Unit,
    onAuthorizeClick: () -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        value = email,
        hint = stringResource(id = R.string.hint_email),
        onValueChange = onEmailChange,
        showEmailHint = true,
    ) {
        JobisSmallButton(
            text = stringResource(id = R.string.authorize),
            onClick = onAuthorizeClick,
            keyboardInteractionEnabled = false,
        )
    }
    JobisTextField(
        title = stringResource(id = R.string.authentication_code),
        value = authenticationCode,
        hint = stringResource(id = R.string.hint_authentication_code),
        onValueChange = onAuthenticationCodeChange,
    )
}
