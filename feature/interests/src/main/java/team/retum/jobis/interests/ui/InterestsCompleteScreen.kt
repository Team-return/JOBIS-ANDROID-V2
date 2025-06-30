package team.retum.jobis.interests.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.retum.jobis.interests.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
internal fun InterestsComplete(
    onBackPressed: () -> Unit,
    studentName: String,
) {
    InterestsCompleteScreen(
        studentName = studentName,
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun InterestsCompleteScreen(
    studentName: String,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisSmallTopAppBar(
            title = stringResource(R.string.set_interests),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(team.retum.design_system.R.drawable.success),
                contentDescription = "major_check_success",
            )
            JobisText(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
                text = stringResource(R.string.interests_check_title, studentName),
                textAlign = TextAlign.Center,
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
                textAlign = TextAlign.Center,
                style = JobisTypography.SubBody,
            )
        }
    }
}
