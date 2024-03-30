package team.retum.jobis.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.jobis.splash.R
import team.retum.jobis.splash.viewmodel.SplashSideEffect
import team.retum.jobis.splash.viewmodel.SplashViewModel
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.dialog.JobisDialog
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast

@Composable
internal fun Splash(
    navigateToLanding: () -> Unit,
    navigateToRoot: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        with(splashViewModel) {
            getAccessToken()
            splashViewModel.sideEffect.collect {
                when (it) {
                    is SplashSideEffect.MoveToLanding -> {
                        navigateToLanding()
                    }

                    is SplashSideEffect.MoveToMain -> {
                        navigateToRoot()
                    }

                    is SplashSideEffect.ShowCheckServerDialog -> {
                        setShowDialog(true)
                    }

                    is SplashSideEffect.ShowOfflineToast -> {
                        JobisToast.create(
                            context = context,
                            message = context.getString(R.string.toast_check_internet_connection),
                            drawable = JobisIcon.Error,
                        ).show()
                    }
                }
            }
        }
    }

    if (showDialog) {
        JobisDialog(
            onDismissRequest = { setShowDialog(false) },
            title = stringResource(id = R.string.check_server_title),
            description = stringResource(id = R.string.check_server),
            mainButtonText = stringResource(id = R.string.ok),
            onMainButtonClick = { setShowDialog(false) },
            mainButtonColor = ButtonColor.Primary,
        )
    }
    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.ic_black_jobis
                else R.drawable.ic_white_jobis,
            ),
            contentDescription = "jobis",
        )
        Image(
            modifier = Modifier.align(Alignment.BottomCenter),
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.ic_black_team_return
                else R.drawable.ic_white_team_return,
            ),
            contentDescription = "team return",
        )
    }
}
