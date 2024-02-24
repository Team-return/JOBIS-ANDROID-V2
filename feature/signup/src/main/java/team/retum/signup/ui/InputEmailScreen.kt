package team.retum.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisSmallButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.signup.R
import team.retum.signup.viewmodel.InputEmailSideEffect
import team.retum.signup.viewmodel.InputEmailState
import team.retum.signup.viewmodel.InputEmailViewModel
import java.io.Serializable

@Composable
internal fun InputEmail(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    signUpData: () -> Serializable,
    inputEmailViewModel: InputEmailViewModel = hiltViewModel(),
) {
    val state by inputEmailViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        inputEmailViewModel.sideEffect.collect {
            when(it){
                is InputEmailSideEffect.MoveToInputPassword -> onNextClick()
            }
        }
    }

    InputEmailScreen(
        onBackPressed = onBackPressed,
        onNextClick = inputEmailViewModel::onNextClick,
        state = state,
        onEmailChange = inputEmailViewModel::onEmailChange,
        onAuthenticationCodeChange = inputEmailViewModel::onAuthenticationCodeChange,
        onAuthenticationClick = inputEmailViewModel::onAuthenticationClick,
    )
}

@Composable
private fun InputEmailScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: InputEmailState,
    onEmailChange: (String) -> Unit,
    onAuthenticationCodeChange: (String) -> Unit,
    onAuthenticationClick: () -> Unit,
) {
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
            email = { state.email },
            onEmailChange = onEmailChange,
            authenticationCode = { state.authenticationCode },
            onAuthenticationCodeChange = onAuthenticationCodeChange,
            onAuthenticationClick = onAuthenticationClick,
            emailDescriptionType = state.emailDescriptionType,
            showEmailDescription = { state.showEmailDescription },
            showAuthenticationCodeDescription = { state.showAuthenticationCodeDescription },
            sendAuthenticationCode = { state.sendAuthenticationCode },
            remainTime = state.remainTime,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.next),
            color = ButtonColor.Primary,
            onClick = onNextClick,
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun EmailInputs(
    email: () -> String,
    authenticationCode: () -> String,
    onEmailChange: (String) -> Unit,
    onAuthenticationCodeChange: (String) -> Unit,
    onAuthenticationClick: () -> Unit,
    emailDescriptionType: DescriptionType,
    showEmailDescription: () -> Boolean,
    showAuthenticationCodeDescription: () -> Boolean,
    sendAuthenticationCode: () -> Boolean,
    remainTime: String,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        value = email,
        hint = stringResource(id = R.string.hint_email),
        onValueChange = onEmailChange,
        showEmailHint = true,
        checkDescription = stringResource(id = R.string.description_email_sent),
        errorDescription = stringResource(id = R.string.description_conflict_email),
        showDescription = showEmailDescription,
        descriptionType = emailDescriptionType,
    ) {
        JobisSmallButton(
            text = stringResource(
                id = if (sendAuthenticationCode()) R.string.re_send_authentication_code
                else R.string.authentication,
            ),
            color = ButtonColor.Secondary,
            onClick = onAuthenticationClick,
            keyboardInteractionEnabled = false,
        )
    }
    JobisTextField(
        title = stringResource(id = R.string.authentication_code),
        value = authenticationCode,
        hint = stringResource(id = R.string.hint_authentication_code),
        onValueChange = onAuthenticationCodeChange,
        errorDescription = stringResource(id = R.string.description_invalid_authentication_code),
        showDescription = showAuthenticationCodeDescription,
        descriptionType = DescriptionType.Error,
    ) {
        JobisText(
            text = remainTime,
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}
