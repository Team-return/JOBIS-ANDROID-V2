package team.retum.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText

const val NAVIGATION_LANDING = "landing"

fun NavGraphBuilder.landing() {
    composable(route = NAVIGATION_LANDING) {
        Landing()
    }
}

@Composable
private fun Landing() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        showButton = true
    }

    LandingScreen(
        composition = composition,
        showButton = showButton,
    )
}

@Composable
private fun LandingScreen(
    composition: LottieComposition?,
    showButton: Boolean,
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
                onClick = {},
                color = ButtonColor.Primary,
            )
            JobisText(
                modifier = Modifier.padding(bottom = 24.dp),
                text = "기존 계정으로 로그인하기",
                style = JobisTypography.SubBody,
                textDecoration = TextDecoration.Underline,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
