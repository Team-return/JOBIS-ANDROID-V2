package team.retum.jobisdesignsystemv2.tab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    tabs: List<String>,
    onSelectTab: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            )
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.inverseSurface)
            .horizontalScroll(rememberScrollState()),
    ) {
        tabs.forEachIndexed { index, category ->
            val textColor by animateColorAsState(
                targetValue = if (index == selectedTabIndex) {
                    JobisTheme.colors.onBackground
                } else {
                    JobisTheme.colors.onSurfaceVariant
                },
                label = "",
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (index == selectedTabIndex) {
                    JobisTheme.colors.background
                } else {
                    JobisTheme.colors.inverseSurface
                },
                label = "",
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow,
                ),
            )

            JobisText(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        enabled = true,
                        onPressed = {},
                        onClick = { onSelectTab(index) },
                    )
                    .background(backgroundColor)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 10.dp,
                    ),
                text = category,
                style = JobisTypography.Body,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}
