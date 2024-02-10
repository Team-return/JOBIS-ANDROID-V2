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
import team.retum.signup.R
import team.retum.signup.viewmodel.InputPersonalInfoState
import team.retum.signup.viewmodel.InputPersonalInfoViewModel
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.textfield.DescriptionType
import team.returm.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun InputPersonalInfo(
    onBackPressed: () -> Unit,
    navigateToInputEmail: (name: String, number: Long) -> Unit,
    inputPersonalInfoViewModel: InputPersonalInfoViewModel = hiltViewModel(),
) {
    val state by inputPersonalInfoViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        inputPersonalInfoViewModel.sideEffect.collect {
            navigateToInputEmail(state.name.trim(), state.number.trim().toLong())
        }
    }

    InputPersonalInfoScreen(
        onBackPressed = onBackPressed,
        state = state,
        onNameChange = inputPersonalInfoViewModel::setName,
        onNumberChange = inputPersonalInfoViewModel::setNumber,
        onNextClick = inputPersonalInfoViewModel::onNextClick,
    )
}

@Composable
private fun InputPersonalInfoScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: InputPersonalInfoState,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.input_personal_information),
            onBackPressed = onBackPressed,
        )
        PersonalInformationInputs(
            name = { state.name },
            number = { state.number },
            onNameChange = onNameChange,
            onNumberChange = onNumberChange,
            showNameDescription = { state.showNameDescription },
            showNumberDescription = { state.showNumberDescription },
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
fun PersonalInformationInputs(
    name: () -> String,
    number: () -> String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    showNameDescription: () -> Boolean,
    showNumberDescription: () -> Boolean,
) {
    JobisTextField(
        title = stringResource(id = R.string.name),
        value = name,
        onValueChange = onNameChange,
        hint = stringResource(id = R.string.hint_name),
        errorDescription = stringResource(id = R.string.description_invalid_name),
        descriptionType = DescriptionType.Error,
        showDescription = showNameDescription,
        imeAction = ImeAction.Next,
    )
    JobisTextField(
        title = stringResource(id = R.string.student_number),
        value = number,
        onValueChange = onNumberChange,
        hint = stringResource(id = R.string.hint_student_number),
        errorDescription = stringResource(id = R.string.description_invalid_number),
        descriptionType = DescriptionType.Error,
        showDescription = showNumberDescription,
        keyboardType = KeyboardType.NumberPassword,
    )
}
