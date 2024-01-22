package team.retum.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.returm.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.textfield.JobisTextField

const val NAVIGATION_SIGN_IN = "signIn"

fun NavGraphBuilder.signIn() {
    composable(route = NAVIGATION_SIGN_IN) {
        SignIn()
    }
}

@Composable
private fun SignIn() {
    SignInScreen()
}

@Composable
private fun SignInScreen() {
    // TODO 뷰모델로 옮기기
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisSmallTopAppBar(onBackPressed = {})
        SignInScreenTitle()
        SignInInputs(
            email = { email },
            password = { password },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(id = R.string.sign_in),
            onClick = {},
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun SignInInputs(
    email: () -> String,
    password: () -> String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    JobisTextField(
        title = stringResource(id = R.string.email),
        showEmail = true,
        hint = "example",
        value = email,
        onValueChange = onEmailChange,
    )
    JobisTextField(
        title = stringResource(id = R.string.password),
        hint = "비밀번호를 입력해주세요",
        value = password,
        onValueChange = onPasswordChange,
    )
}

@Composable
private fun SignInScreenTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = JobisTheme.colors.onPrimary)) {
                    append("JOBIS")
                }
                withStyle(style = SpanStyle(color = JobisTheme.colors.onBackground)) {
                    append("에 로그인하기")
                }
            },
            style = JobisTypography.PageTitle,
        )
        Divider(
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
