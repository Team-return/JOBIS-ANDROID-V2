package team.retum.signin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisMediumButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.signin.R
import team.retum.signin.viewmodel.SignInSideEffect
import team.retum.signin.viewmodel.SignInState
import team.retum.signin.viewmodel.SignInViewModel

@Composable
internal fun SignIn(
    onBackClick: () -> Unit,
    onSignInSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    val state by signInViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        signInViewModel.sideEffect.collect {
            when (it) {
                is SignInSideEffect.Success -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_success),
                    ).show()
                    onSignInSuccess()
                }

                is SignInSideEffect.BadRequest -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_error),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is SignInSideEffect.CheckInternetConnection -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_check_internet_connection),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is SignInSideEffect.ServerTimeOut -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_connection_time_out),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    SignInScreen(
        onBackClick = onBackClick,
        state = state,
        onEmailChange = signInViewModel::setEmail,
        onPasswordChange = signInViewModel::setPassword,
        onSignInClick = signInViewModel::signIn,
        onForgotPasswordClick = onForgotPasswordClick,
    )
}

@Composable
private fun SignInScreen(
    onBackClick: () -> Unit,
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackClick)
        SignInScreenTitle()
        SignInInputs(
            email = { state.email },
            password = { state.password },
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            notFoundEmail = { state.showEmailDescription },
            invalidPassword = { state.showPasswordDescription },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JobisMediumButton(
            text = stringResource(id = R.string.forgot_password),
            drawable = painterResource(id = JobisIcon.LockReset),
            onClick = onForgotPasswordClick,
            keyboardInteractionEnabled = false,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.testTag(stringResource(id = R.string.sign_in)),
            text = stringResource(id = R.string.sign_in),
            onClick = onSignInClick,
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun SignInInputs(
    email: () -> String,
    password: () -> String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    notFoundEmail: () -> Boolean,
    invalidPassword: () -> Boolean,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        showEmailHint = true,
        hint = stringResource(id = R.string.hint_email),
        value = email,
        onValueChange = onEmailChange,
        errorDescription = stringResource(id = R.string.description_email_not_found),
        descriptionType = DescriptionType.Error,
        showDescription = notFoundEmail,
        testTag = stringResource(id = R.string.email),
    )
    JobisTextField(
        title = stringResource(id = R.string.password),
        hint = stringResource(id = R.string.hint_password),
        value = password,
        onValueChange = onPasswordChange,
        showVisibleIcon = true,
        errorDescription = stringResource(id = R.string.description_password_invalid),
        showDescription = invalidPassword,
        descriptionType = DescriptionType.Error,
        testTag = stringResource(id = R.string.password),
    )
}

@Composable
private fun SignInScreenTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 20.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary)) {
                    append(stringResource(id = R.string.jobis))
                }
                withStyle(style = SpanStyle(color = JobisTheme.colors.onBackground)) {
                    append(stringResource(id = R.string.to_login_in_to))
                }
            },
            style = JobisTypography.PageTitle,
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            color = JobisTheme.colors.onBackground,
        )
        Image(
            painter = painterResource(id = JobisIcon.MeetingRoom),
            contentDescription = "sign in screen title icon",
        )
    }
}
