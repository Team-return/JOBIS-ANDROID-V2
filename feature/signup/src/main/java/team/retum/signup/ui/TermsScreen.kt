package team.retum.signup.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.signup.R
import team.retum.signup.model.SignUpData
import team.retum.signup.viewmodel.TermsSideEffect
import team.retum.signup.viewmodel.TermsState
import team.retum.signup.viewmodel.TermsViewModel

private const val TERMS_URL = "https://jobis-webview.team-return.com/sign-up-policy"
private const val WEB_VIEW_SCROLL_BOTTOM = 1

@Composable
internal fun Terms(
    onBackPressed: () -> Unit,
    navigateToRoot: () -> Unit,
    signUpData: SignUpData,
    termsViewModel: TermsViewModel = hiltViewModel(),
) {
    val state by termsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        termsViewModel.sideEffect.collect {
            when (it) {
                is TermsSideEffect.Success -> {
                    navigateToRoot()
                }
            }
        }
    }

    TermsScreen(
        onBackPressed = onBackPressed,
        onCompleteClick = { termsViewModel.onCompleteClick(signUpData = signUpData) },
        state = state,
        onReachTheEnded = termsViewModel::onReachTheEnded,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun TermsScreen(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
    state: TermsState,
    onReachTheEnded: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.terms),
            onBackPressed = onBackPressed,
        )
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = {
                WebView(it).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    setOnScrollChangeListener { _, _, _, _, _ ->
                        onReachTheEnded(!canScrollVertically(WEB_VIEW_SCROLL_BOTTOM))
                    }
                    loadUrl(TERMS_URL)
                }
            },
            update = {
                it.loadUrl(TERMS_URL)
            },
        )
        JobisButton(
            text = stringResource(id = R.string.agree),
            onClick = onCompleteClick,
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
        )
    }
}
