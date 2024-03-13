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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.signup.R
import team.retum.signup.model.SignUpData
import team.retum.signup.viewmodel.SetPasswordSideEffect
import team.retum.signup.viewmodel.SetPasswordState
import team.retum.signup.viewmodel.SetPasswordViewModel

@Composable
internal fun SetPassword(
    onBackPressed: () -> Unit,
    navigateToSelectGender: (SignUpData) -> Unit,
    signUpData: SignUpData,
    setPasswordViewModel: SetPasswordViewModel = hiltViewModel(),
) {
    val state by setPasswordViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        setPasswordViewModel.sideEffect.collect {
            when (it) {
                is SetPasswordSideEffect.MoveToNext -> {
                    navigateToSelectGender(signUpData.copy(password = it.password))
                }
            }
        }
    }

    SetPasswordScreen(
        onBackPressed = onBackPressed,
        onNextClick = setPasswordViewModel::onNextClick,
        state = state,
        onPasswordChange = setPasswordViewModel::setPassword,
        onRepeatPasswordChange = setPasswordViewModel::setRepeatPassword,
    )
}

@Composable
private fun SetPasswordScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: SetPasswordState,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
) {
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
            password = { state.password },
            repeatPassword = { state.repeatPassword },
            onPasswordChange = onPasswordChange,
            onCheckPassword = onRepeatPasswordChange,
            showPasswordDescription = { state.showPasswordDescription },
            showRepeatPasswordDescription = { state.showRepeatPasswordDescription },
            passwordDescriptionType = state.passwordDescriptionType,
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
private fun PasswordInputs(
    password: () -> String,
    repeatPassword: () -> String,
    onPasswordChange: (String) -> Unit,
    onCheckPassword: (String) -> Unit,
    showPasswordDescription: () -> Boolean,
    showRepeatPasswordDescription: () -> Boolean,
    passwordDescriptionType: DescriptionType,
) {
    JobisTextField(
        title = stringResource(id = R.string.password),
        value = password,
        onValueChange = onPasswordChange,
        hint = stringResource(id = R.string.hint_password),
        showDescription = showPasswordDescription,
        informationDescription = stringResource(id = R.string.password_information_description),
        errorDescription = stringResource(id = R.string.password_error_description),
        descriptionType = passwordDescriptionType,
        showVisibleIcon = true,
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Password,
    )
    JobisTextField(
        title = stringResource(id = R.string.check_password),
        value = repeatPassword,
        onValueChange = onCheckPassword,
        hint = stringResource(id = R.string.hint_check_password),
        showDescription = showRepeatPasswordDescription,
        errorDescription = stringResource(id = R.string.repeat_password_error_description),
        descriptionType = DescriptionType.Error,
        showVisibleIcon = true,
    )
}
