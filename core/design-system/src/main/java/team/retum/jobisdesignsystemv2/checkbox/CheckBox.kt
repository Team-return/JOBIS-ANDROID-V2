package team.retum.jobisdesignsystemv2.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun CheckBox(
    checked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (checked) JobisTheme.colors.background
        else JobisTheme.colors.surfaceTint,
        label = "",
    )
    val background by animateColorAsState(
        targetValue = if (checked) JobisTheme.colors.onPrimary
        else JobisTheme.colors.inverseSurface,
        label = "",
    )

    Box(
        modifier = Modifier
            .clickable(
                enabled = true,
                onPressed = {},
                onClick = { onClick(!checked) },
                pressDepth = 0.95f,
            )
            .clip(RoundedCornerShape(6.dp))
            .background(color = background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 8.dp,
            ),
            painter = painterResource(id = JobisIcon.Check),
            contentDescription = "check",
            tint = tint,
        )
    }
}
