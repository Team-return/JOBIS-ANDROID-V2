package team.retum.jobis.verify.email.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.jobis.verify.email.viewmodel.EMAIL_ADDRESS
import team.retum.jobis.verify.email.viewmodel.VerifyEmailSideEffect
import team.retum.jobis.verify.email.viewmodel.VerifyEmailState
import team.retum.jobis.verify.email.viewmodel.VerifyEmailViewModel
import team.retum.jobis.verify_email.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisSmallButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast

@Composable
internal fun VerifyEmail(
    onBackPressed: () -> Unit,
    navigateToResetPassword: (type: ResetPasswordNavigationArgumentType, email: String) -> Unit,
    verifyEmailViewModel: VerifyEmailViewModel = hiltViewModel(),
) {
    val state by verifyEmailViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        verifyEmailViewModel.sideEffect.collect {
            when (it) {
                is VerifyEmailSideEffect.MoveToVerifyPassword -> {
                    navigateToResetPassword(
                        ResetPasswordNavigationArgumentType.EMAIL,
                        state.email + EMAIL_ADDRESS,
                    )
                }

                is VerifyEmailSideEffect.AuthenticationCodeExpiration -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.description_authentication_code_expired),
                    ).show()
                }

                is VerifyEmailSideEffect.CheckEmailValidation -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_check_email_validation),
                    ).show()
                }
            }
        }
    }

    VerifyEmailScreen(
        onBackPressed = onBackPressed,
        onNextClick = verifyEmailViewModel::onNextClick,
        state = state,
        onEmailChange = verifyEmailViewModel::setEmail,
        onAuthenticationCodeChange = verifyEmailViewModel::onAuthenticationCodeChange,
        onAuthenticationClick = verifyEmailViewModel::onAuthenticationClick,
    )
}

@Composable
private fun VerifyEmailScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: VerifyEmailState,
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
            title = stringResource(id = R.string.title_change_password),
            onBackPressed = onBackPressed,
        )
        VerifyEmailInputs(
            email = { state.email },
            authenticationCode = { state.authenticationCode },
            onEmailChange = onEmailChange,
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
            onClick = onNextClick,
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun VerifyEmailInputs(
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
                id = if (sendAuthenticationCode()) {
                    R.string.re_send_authentication_code
                } else {
                    R.string.authentication
                },
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
        keyboardType = KeyboardType.NumberPassword,
    ) {
        JobisText(
            text = remainTime,
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}
