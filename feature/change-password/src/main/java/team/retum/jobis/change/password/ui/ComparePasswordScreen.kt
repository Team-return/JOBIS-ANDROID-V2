package team.retum.jobis.change.password.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.jobis.change.password.viewmodel.ComparePasswordSideEffect
import team.retum.jobis.change.password.viewmodel.ComparePasswordState
import team.retum.jobis.change.password.viewmodel.ComparePasswordViewModel
import team.retum.jobis.change_password.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun ComparePassword(
    onBackPressed: () -> Unit,
    navigateToResetPassword: (type: ResetPasswordNavigationArgumentType, currentPassword: String) -> Unit,
    comparePasswordViewModel: ComparePasswordViewModel = hiltViewModel(),
) {
    val state by comparePasswordViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        comparePasswordViewModel.sideEffect.collect {
            if (it is ComparePasswordSideEffect.Success) {
                navigateToResetPassword(
                    ResetPasswordNavigationArgumentType.PASSWORD,
                    state.password,
                )
            }
        }
    }

    ComparePasswordScreen(
        onBackPressed = onBackPressed,
        onNextClick = comparePasswordViewModel::comparePassword,
        state = state,
        onPasswordChange = comparePasswordViewModel::setPassword,
    )
}

@Composable
private fun ComparePasswordScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: ComparePasswordState,
    onPasswordChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.title_confirm_password),
            onBackPressed = onBackPressed,
        )
        JobisTextField(
            title = stringResource(id = R.string.password),
            value = { state.password },
            onValueChange = onPasswordChange,
            hint = stringResource(id = R.string.hint_password_confirm),
            showVisibleIcon = true,
            showDescription = { state.showPasswordDescription },
            descriptionType = DescriptionType.Error,
            errorDescription = stringResource(id = R.string.password_error_description),
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
