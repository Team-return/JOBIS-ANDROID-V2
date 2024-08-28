package team.retum.landing.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import team.retum.common.utils.notificationPermissionGranted
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.landing.R

private fun ManagedActivityResultLauncher<String, Boolean>.requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
internal fun Landing(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val context = LocalContext.current
    val settingLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {}
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                JobisToast.create(
                    context = context,
                    message = context.getString(R.string.toast_message_notificaiton_revoked),
                    drawable = JobisIcon.Error,
                ).show()
                settingLauncher.launch(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    ),
                )
            }
        }

    LaunchedEffect(Unit) {
        delay(500L)
        if (!notificationPermissionGranted(context)) {
            requestPermissionLauncher.requestNotificationPermission()
        }
    }

    LandingScreen(
        onSignInClick = onSignInClick,
        onSignUpClick = onSignUpClick,
    )
}

@Composable
private fun LandingScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        if (isSystemInDarkTheme()) {
            LottieCompositionSpec.RawRes(R.raw.splash_black)
        } else {
            LottieCompositionSpec.RawRes(R.raw.splash_white)
        },
    )
    Box {
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .fillMaxSize()
                .background(JobisTheme.colors.background),
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            JobisButton(
                text = stringResource(id = R.string.start_new_account),
                onClick = onSignUpClick,
                color = ButtonColor.Primary,
                keyboardInteractionEnabled = false,
            )
            JobisText(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable(
                        onClick = onSignInClick,
                        indication = rememberRipple(),
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                text = stringResource(id = R.string.login_with_existing_account),
                style = JobisTypography.SubBody,
                textDecoration = TextDecoration.Underline,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
