package team.retum.signup.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme

private const val TERMS_URL = "https://jobis-webview.team-return.com/sign-up-policy"
private const val WEB_VIEW_HEIGHT = 0.85f

@Composable
fun Terms(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    TermsScreen(
        onBackPressed = onBackPressed,
        onCompleteClick = onCompleteClick,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun TermsScreen(
    onBackPressed: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = stringResource(id = R.string.terms),
            onBackPressed = onBackPressed,
        )
        AndroidView(
            modifier = Modifier.fillMaxHeight(WEB_VIEW_HEIGHT),
            factory = {
                WebView(it).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true

                    loadUrl(TERMS_URL)
                }
            },
            update = {
                it.loadUrl(TERMS_URL)
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp,
            ),
            text = stringResource(id = R.string.agree),
            onClick = onCompleteClick,
            color = ButtonColor.Primary,
        )
    }
}
