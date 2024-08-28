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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import java.lang.Float.max

/**
 * CollapsingTopAppBar에서 앱바 애니메이션이 동작할 때 위, 아래 간격을 조정하기 위한 상수
 */
internal object AppBarPaddings {
    const val TOP = 64f
    const val BOTTOM = 20f
}

/**
 * JOBIS의 TopBar 속성을 추상화 시켜놓은 Basic 함수
 *
 * @param modifier TopAppBar마다 다른 크기를 지정하기 위해 사용
 * @param showLogo JOBIS 로고를 표시할지 말지 여부를 결정
 * @param onBackPressed 뒤로가기 버튼을 눌렀을 때 동작할 함수
 * @param actions 사용자가 수행할 수 있는 행동 목록 정의
 *
 * 다음과 같이 사용한다.
 * ```
 * JobisLargeTopAppBar(title = stringResource(id = R.string.recruitment)) {
 *      JobisIconButton(
 *          painter = painterResource(JobisIcon.Filter),
 *          contentDescription = "filter",
 *          onClick = onRecruitmentFilterClick,
 *          tint = JobisTheme.colors.onPrimary,
 *     )
 *     JobisIconButton(
 *         painter = painterResource(JobisIcon.Search),
 *         contentDescription = "search",
 *         onClick = onSearchRecruitmentClick,
 *     )
 * }
 * ```
 *
 * @param title 사용하는 화면에서 중앙에 표시될 텍스트
 */
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
                    drawableResId = JobisIcon.Arrow,
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
 * 크기가 작은 TopAppBar
 *
 * 뷰의 미리보기와 각 파라미터에 대한 설명은 다음 함수의 주석을 참고한다.
 * @see [JobisSmallTopAppBarPreview]
 * @see [BasicTopAppBar]
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
 * 크기가 큰 TopAppBar
 *
 * 뷰의 미리보기와 각 파라미터에 대한 설명은 다음 함수의 주석을 참고한다.
 * @see [JobisLargeTopAppBarPreview]
 * @see [BasicTopAppBar]
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
 * 리스트뷰에 따라 크기가 동적으로 변하는 TopAppBar
 *
 * @param scrollState 해당 TopAppBar와 함께 동작할 ScrollView의 [ScrollState]
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
    var topPadding by remember { mutableFloatStateOf(AppBarPaddings.TOP) }
    var bottomPadding by remember { mutableFloatStateOf(AppBarPaddings.BOTTOM) }

    var titleAlpha by remember { mutableFloatStateOf(1f) }

    val onScrollChanged = { scrollPercentage: Float ->
        titleAlpha = 1f - scrollPercentage

        with(AppBarPaddings) {
            val calculatedTopPadding = TOP.run {
                minus(scrollPercentage * this)
            }

            val calculatedBottomPadding = BOTTOM.run {
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

@Preview
@Composable
private fun JobisSmallTopAppBarPreview() {
    JobisDesignSystemV2Theme {
        JobisSmallTopAppBar(
            title = "기업 목록",
            onBackPressed = {},
        ) {
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = "search",
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun JobisLargeTopAppBarPreview() {
    JobisDesignSystemV2Theme {
        JobisLargeTopAppBar(title = "모집의뢰서") {
            JobisIconButton(
                drawableResId = JobisIcon.Filter,
                contentDescription = "filter",
                onClick = {},
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = "search",
                onClick = {},
            )
        }
    }
}
