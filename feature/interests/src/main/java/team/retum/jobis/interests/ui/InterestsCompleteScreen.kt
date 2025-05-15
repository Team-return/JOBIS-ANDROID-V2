package team.retum.jobis.interests.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.interests.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
fun InterestsComplete(
    onBackPressed: () -> Unit,
    navigateToMyPage: () -> Unit,
) {
    InterestsCompleteScreen(
        onBackPressed = onBackPressed,
        navigateToMyPage = navigateToMyPage,
    )
}

@Composable
fun InterestsCompleteScreen(
    onBackPressed: () -> Unit,
    navigateToMyPage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        JobisSmallTopAppBar(
            title = "관심 분야 설정",
            onBackPressed = onBackPressed,
        )
        Image(
            painter = painterResource(team.retum.design_system.R.drawable.success),
            contentDescription = null,
        )
        JobisText(
            modifier = Modifier.padding(
                top = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_check_title),
            style = JobisTypography.PageTitle,
            color = JobisTheme.colors.onBackground,
        )
        JobisText(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_alarm),
            style = JobisTypography.SubBody
        )
        JobisButton(
            text = "마이페이지로 이동하기",
            onClick = navigateToMyPage,
        )
    }
}
