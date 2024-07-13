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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

/**
 * navigation에 정의되는 로그인 함수
 *
 * @param onBackClick 로그인 화면에서 뒤로가기 버튼을 눌렀을 때 동작하는 함수
 * @param onSignInSuccess 로그인 화면에서 로그인 성공 시 동작하는 함수
 * @param onForgotPasswordClick 로그인 화면에서 비밀번호 찾기 텍스트를 클릭했을 때 동작하는 함수
 * @param signInViewModel [SignInState], [SignInSideEffect]를 관리하는 로그인 뷰모델
 */
@Composable
internal fun SignIn(
    onBackClick: () -> Unit,
    onSignInSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    val state by signInViewModel.state.collectAsStateWithLifecycle()
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

/**
 * 로그인 화면을 담당하는 컴포저블 함수
 *
 * @param state 로그인 화면에서 사용되는 email, password 등의 상태를 담고 있는 데이터 클래스
 * @param onEmailChange 사용자가 입력한 이메일이 변경될 때마다 동작하는 함수
 * @param onPasswordChange 사용자가 입력한 비밀번호가 변경될 때마다 동작하는 함수
 * @param onSignInClick 로그인 버튼 클릭 시 동작하는 함수
 */
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
            showNotFoundEmailDescription = { state.showEmailDescription },
            showInvalidPasswordDescription = { state.showPasswordDescription },
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

/**
 * 로그인 인풋을 담당하는 컴포저블 함수
 *
 * @param email [SignInState.email]을 반환하는 함수
 * @param password [SignInState.password]를 반환하는 함수
 * @param onEmailChange [SignInState.email]을 업데이트 하는 함수
 * @param onPasswordChange [SignInState.password]를 업데이트 하는 함수
 * @param showNotFoundEmailDescription [R.string.description_email_not_found]표시를 결정하는 booelan 값 반환 함수
 * @param showInvalidPasswordDescription [R.string.description_password_invalid]표를 결정하는 boolean 값 반환 함수
 */
@Composable
private fun SignInInputs(
    email: () -> String,
    password: () -> String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    showNotFoundEmailDescription: () -> Boolean,
    showInvalidPasswordDescription: () -> Boolean,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        showEmailHint = true,
        hint = stringResource(id = R.string.hint_email),
        value = email,
        onValueChange = onEmailChange,
        errorDescription = stringResource(id = R.string.description_email_not_found),
        descriptionType = DescriptionType.Error,
        showDescription = showNotFoundEmailDescription,
        testTag = stringResource(id = R.string.email),
    )
    JobisTextField(
        title = stringResource(id = R.string.password),
        hint = stringResource(id = R.string.hint_password),
        value = password,
        onValueChange = onPasswordChange,
        showVisibleIcon = true,
        errorDescription = stringResource(id = R.string.description_password_invalid),
        showDescription = showInvalidPasswordDescription,
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
