package team.retum.jobisdesignsystemv2.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

/**
 * This composable function creates a JobisChip element for use in Jobis.
 *
 * @param text Text to be displayed on the chip
 * @param selected Whether the chip is selected
 * @param onClick Called when this chip is clicked
 * @param modifier The modifier to be applied to the JobisChip
 */
@Composable
fun JobisChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.onPrimary else JobisTheme.colors.inverseSurface,
        label = "",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.background else JobisTheme.colors.onPrimaryContainer,
        label = "",
    )

    Box(
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = onClick,
                onPressed = {},
            )
            .clip(RoundedCornerShape(30.dp))
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
            text = text,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}
