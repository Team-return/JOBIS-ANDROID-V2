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
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.button.JobisSmallButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText
import team.returm.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun InputEmail(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    InputEmailScreen(
        onBackPressed = onBackPressed,
        onNextClick = onNextClick,
    )
}

@Composable
private fun InputEmailScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    // TODO: viewModel로 옮기기
    var email by remember { mutableStateOf("") }
    var authenticationCode by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.input_email),
            onBackPressed = onBackPressed,
        )
        EmailInputs(
            email = { email },
            authenticationCode = { authenticationCode },
            onEmailChange = { email = it },
            onAuthenticationCodeChange = { authenticationCode = it },
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
private fun EmailInputs(
    email: () -> String,
    authenticationCode: () -> String,
    onEmailChange: (String) -> Unit,
    onAuthenticationCodeChange: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        value = email,
        hint = stringResource(id = R.string.hint_email),
        onValueChange = onEmailChange,
        showEmailHint = true,
    ) {
        JobisSmallButton(
            text = "인증 하기",
            color = ButtonColor.Secondary,
            onClick = {},
            keyboardInteractionEnabled = false,
        )
    }
    JobisTextField(
        title = stringResource(id = R.string.authentication_code),
        value = authenticationCode,
        hint = stringResource(id = R.string.hint_authentication_code),
        onValueChange = onAuthenticationCodeChange,
    ) {
        JobisText(
            text = "05:00",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}
