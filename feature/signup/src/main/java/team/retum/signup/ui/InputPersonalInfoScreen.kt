package team.retum.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.textfield.JobisTextField

@Composable
fun InputPersonalInfoScreen(
    onBackClick: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.input_personal_information),
            onBackPressed = onBackClick,
        )
        PersonalInformationInputs(
            name = { name },
            studentNumber = { studentNumber },
            onNameChange = { name = it },
            onStudentNumberChange = { studentNumber = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(id = R.string.next),
            color = ButtonColor.Primary,
            onClick = {},
        )
    }
}

@Composable
fun PersonalInformationInputs(
    name: () -> String,
    studentNumber: () -> String,
    onNameChange: (String) -> Unit,
    onStudentNumberChange: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.name),
        value = name,
        onValueChange = onNameChange,
        hint = stringResource(id = R.string.hint_name),
    )
    JobisTextField(
        title = stringResource(id = R.string.student_number),
        value = studentNumber,
        onValueChange = onStudentNumberChange,
        hint = stringResource(id = R.string.hint_student_number),
    )
}
