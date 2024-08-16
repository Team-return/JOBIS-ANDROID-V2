package team.retum.jobisdesignsystemv2.switches

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

@Composable
fun JobisSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: @Composable() (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = JobisSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) = Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    thumbContent = thumbContent,
    enabled = enabled,
    colors = colors,
    interactionSource = interactionSource,
)

object JobisSwitchDefaults {

    @Composable
    fun colors(
        checkedThumbColor: Color = JobisTheme.colors.background,
        checkedTrackColor: Color = JobisTheme.colors.primaryContainer,
        checkedBorderColor: Color = Color.Transparent,
        checkedIconColor: Color = JobisTheme.colors.primaryContainer,
        uncheckedThumbColor: Color = JobisTheme.colors.background,
        uncheckedTrackColor: Color = JobisTheme.colors.surfaceTint,
        uncheckedBorderColor: Color = Color.Transparent,
        uncheckedIconColor: Color = JobisTheme.colors.surfaceTint,
        disabledCheckedThumbColor: Color = JobisTheme.colors.surfaceTint,
        disabledCheckedTrackColor: Color = JobisTheme.colors.surfaceVariant,
        disabledCheckedBorderColor: Color = Color.Transparent,
        disabledCheckedIconColor: Color = JobisTheme.colors.surfaceVariant,
        disabledUncheckedThumbColor: Color = JobisTheme.colors.surfaceTint,
        disabledUncheckedTrackColor: Color = JobisTheme.colors.surfaceVariant,
        disabledUncheckedBorderColor: Color = Color.Transparent,
        disabledUncheckedIconColor: Color = JobisTheme.colors.surfaceVariant,
    ) : SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedBorderColor = checkedBorderColor,
        checkedIconColor = checkedIconColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedBorderColor = uncheckedBorderColor,
        uncheckedIconColor = uncheckedIconColor,
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledCheckedBorderColor = disabledCheckedBorderColor,
        disabledCheckedIconColor = disabledCheckedIconColor,
        disabledUncheckedThumbColor = disabledUncheckedThumbColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        disabledUncheckedBorderColor = disabledUncheckedBorderColor,
        disabledUncheckedIconColor = disabledUncheckedIconColor,
    )
}
