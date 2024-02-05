package team.retum.landing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import team.retum.landing.R
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText

@Composable
internal fun Landing(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        showButton = true
    }

    LandingScreen(
        composition = composition,
        showButton = showButton,
        onSignInClick = onSignInClick,
        onSignUpClick = onSignUpClick,
    )
}

@Composable
private fun LandingScreen(
    composition: LottieComposition?,
    showButton: Boolean,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
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
                modifier = Modifier.padding(
                    bottom = 16.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
                text = "새 계정으로 시작하기",
                onClick = onSignUpClick,
                color = ButtonColor.Primary,
            )
            JobisText(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable(
                        onClick = onSignInClick,
                        indication = rememberRipple(),
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                text = "기존 계정으로 로그인하기",
                style = JobisTypography.SubBody,
                textDecoration = TextDecoration.Underline,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
