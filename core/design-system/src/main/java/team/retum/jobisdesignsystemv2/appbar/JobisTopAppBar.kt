package team.retum.jobisdesignsystemv2.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import java.lang.Float.max

internal object AppBarPaddings {
    const val LargeTitleTop = 64f
    const val LargeTitleBottom = 20f
}

@Composable
private fun BasicTopAppBar(
    modifier: Modifier,
    showLogo: Boolean,
    onBackPressed: (() -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)?,
    title: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (showLogo || onBackPressed != null) Arrangement.SpaceBetween else Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showLogo) {
                Image(
                    modifier = Modifier.padding(vertical = 12.dp),
                    painter = painterResource(id = R.drawable.img_jobis),
                    contentDescription = stringResource(id = R.string.content_description_jobis),
                )
            }
            onBackPressed?.run {
                JobisIconButton(
                    modifier = Modifier.padding(vertical = 8.dp),
                    painter = painterResource(id = JobisIcon.Arrow),
                    contentDescription = stringResource(id = R.string.content_description_arrow),
                    onClick = onBackPressed,
                )
            }
            actions?.run {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = this,
                )
            }
        }
        title()
    }
}

/**
 * This composable function creates a JobisSmallTopAppBar element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisSmallTopAppBar.
 * @param title The title text to be displayed in the app bar.
 * @param onBackPressed Callback function for handling the back button press.
 * @param actions An optional composable block to add custom actions to the app bar.
 */
@Composable
fun JobisSmallTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showLogo: Boolean = false,
    onBackPressed: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    BasicTopAppBar(
        modifier = modifier,
        showLogo = showLogo,
        onBackPressed = onBackPressed,
        actions = actions,
    ) {
        JobisText(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = JobisTypography.SubHeadLine,
        )
    }
}

/**
 * This composable function creates a JobisLargeTopAppBar element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisLargeTopAppBar.
 * @param title The title text to be displayed in the app bar.
 * @param onBackPressed Callback function for handling the back button press.
 * @param actions An optional composable block to add custom actions to the app bar.
 */
@Composable
fun JobisLargeTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showLogo: Boolean = false,
    onBackPressed: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    BasicTopAppBar(
        modifier = modifier,
        showLogo = showLogo,
        onBackPressed = onBackPressed,
        actions = actions,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                modifier = Modifier.padding(vertical = 20.dp),
                text = title,
                style = JobisTypography.PageTitle,
            )
        }
    }
}

/**
 * This composable function creates a JobisCollapsingTopAppBar element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisCollapsingTopAppBar.
 * @param title The title text to be displayed in the app bar.
 * @param scrollState The scroll state to determine the scroll position.
 * @param onBackPressed Callback function for handling the back button press.
 * @param actions An optional composable block to add custom actions to the app bar.
 */
@Composable
fun JobisCollapsingTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showLogo: Boolean = false,
    scrollState: ScrollState,
    onBackPressed: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    var topPadding by remember { mutableFloatStateOf(AppBarPaddings.LargeTitleTop) }
    var bottomPadding by remember { mutableFloatStateOf(AppBarPaddings.LargeTitleBottom) }

    var titleAlpha by remember { mutableFloatStateOf(1f) }

    val onScrollChanged = { scrollPercentage: Float ->
        titleAlpha = 1f - scrollPercentage

        with(AppBarPaddings) {
            val calculatedTopPadding = LargeTitleTop.run {
                minus(scrollPercentage * this)
            }

            val calculatedBottomPadding = LargeTitleBottom.run {
                minus(scrollPercentage * this)
            }

            topPadding = max(calculatedTopPadding, 0f)
            bottomPadding = max(calculatedBottomPadding, 0f)
        }
    }

    with(scrollState) {
        LaunchedEffect(value) {
            if (value != 0) {
                onScrollChanged(value.toFloat() / maxValue.toFloat() * 8)
            }
        }
    }

    BasicTopAppBar(
        modifier = modifier,
        showLogo = showLogo,
        onBackPressed = onBackPressed,
        actions = actions,
    ) {
        JobisText(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(1f - titleAlpha * 2),
            text = title,
            style = JobisTypography.SubHeadLine,
        )
        JobisText(
            modifier = Modifier
                .alpha(titleAlpha)
                .align(Alignment.TopStart)
                .padding(
                    top = (topPadding + bottomPadding).dp,
                    bottom = bottomPadding.dp,
                ),
            text = title,
            style = JobisTypography.PageTitle,
        )
    }
}
