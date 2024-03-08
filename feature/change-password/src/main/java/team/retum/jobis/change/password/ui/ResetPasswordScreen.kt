package team.retum.jobis.change.password.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.change.password.viewmodel.ResetPasswordSideEffect
import team.retum.jobis.change.password.viewmodel.ResetPasswordState
import team.retum.jobis.change.password.viewmodel.ResetPasswordViewModel
import team.retum.jobis.change_password.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.toast.JobisToast

@Composable
internal fun ResetPassword(
    onBackPressed: () -> Unit,
    navigateToSignIn: () -> Unit,
    email: String?,
    currentPassword: String?,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
) {
    val state by resetPasswordViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        with(resetPasswordViewModel) {
            setEmail(email)
            setCurrentPassword(currentPassword)
            sideEffect.collect {
                when (it) {
                    is ResetPasswordSideEffect.Success -> {
                        navigateToSignIn()
                        JobisToast.create(
                            context = context,
                            message = context.getString(R.string.success_reset_password),
                        ).show()
                    }

                    is ResetPasswordSideEffect.ClearFocus -> {
                        focusManager.clearFocus()
                    }

                    is ResetPasswordSideEffect.NotFoundEmail -> {
                        JobisToast.create(
                            context = context,
                            message = context.getString(R.string.not_found_email),
                        )
                    }
                }
            }
        }
    }

    ResetPasswordScreen(
        onBackPressed = onBackPressed,
        onCompleteClick = resetPasswordViewModel::onCompleteClick,
        state = state,
        onPasswordChange = resetPasswordViewModel::setPassword,
        onRepeatPasswordChange = resetPasswordViewModel::setRepeatPassword,
    )
}

@Composable
private fun ResetPasswordScreen(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
    state: ResetPasswordState,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
) {
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
            password = { state.password },
            repeatPassword = { state.repeatPassword },
            onPasswordChange = onPasswordChange,
            onCheckPasswordChange = onRepeatPasswordChange,
            showPasswordDescription = { state.showPasswordDescription },
            passwordDescriptionType = state.passwordDescriptionType,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.complete),
            onClick = onCompleteClick,
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
        )
    }
}

@Composable
private fun ResetPasswordInputs(
    password: () -> String,
    repeatPassword: () -> String,
    onPasswordChange: (String) -> Unit,
    onCheckPasswordChange: (String) -> Unit,
    showPasswordDescription: () -> Boolean,
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
        hint = stringResource(id = R.string.hint_check_password),
        value = repeatPassword,
        onValueChange = onCheckPasswordChange,
        showVisibleIcon = true,
    )
}
