package team.retum.jobisdesignsystemv2.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.utils.clickable

/**
 * This composable function creates a JobisCard element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisCard.
 * @param shape Defines the surface's shape as well its shadow.
 * @param background To color inside this card
 * @param enabled Controls the enabled state.
 * @param onClick Called when this card is clicked
 */
@Composable
fun JobisCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    background: Color = JobisTheme.colors.inverseSurface,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (pressed) {
            JobisTheme.colors.surfaceVariant
        } else {
            background
        },
        label = "",
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        onClick = onClick,
                        enabled = enabled,
                        onPressed = { pressed = it },
                    )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        color = backgroundColor,
    ) {
        content()
    }
}
