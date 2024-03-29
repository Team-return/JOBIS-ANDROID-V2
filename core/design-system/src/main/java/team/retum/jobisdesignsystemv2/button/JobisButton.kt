package team.retum.jobisdesignsystemv2.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.foundation.JobisColor
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.DEFAULT_PRESS_DEPTH
import team.retum.jobisdesignsystemv2.utils.DURATION_MILLIS
import team.retum.jobisdesignsystemv2.utils.MIN_PRESS_DEPTH
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.jobisdesignsystemv2.utils.keyboardAsState

private val largeButtonShape = RoundedCornerShape(12.dp)
private val smallButtonShape = RoundedCornerShape(8.dp)
private val mediumButtonShape = RoundedCornerShape(32.dp)

private enum class ButtonType {
    LARGE,
    SMALL,
    DIALOG,
}

@Composable
private fun BasicButton(
    modifier: Modifier,
    backgroundColor: Color,
    shape: RoundedCornerShape,
    enabled: Boolean,
    buttonType: ButtonType,
    keyboardInteractionEnabled: Boolean,
    onPressed: (pressed: Boolean) -> Unit,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val keyboardShow by keyboardAsState()
    val isKeyboardHideButton = keyboardShow && keyboardInteractionEnabled
    val padding = if (isKeyboardHideButton || buttonType == ButtonType.SMALL || buttonType == ButtonType.DIALOG) {
        PaddingValues(
            vertical = 0.dp,
            horizontal = 0.dp,
        )
    } else {
        PaddingValues(
            vertical = 12.dp,
            horizontal = 24.dp,
        )
    }
    val (shapeByKeyboardShow, pressDepth) = if (isKeyboardHideButton) {
        RoundedCornerShape(0.dp) to MIN_PRESS_DEPTH
    } else {
        shape to DEFAULT_PRESS_DEPTH
    }

    Surface(
        modifier = modifier
            .clickable(
                pressDepth = pressDepth,
                enabled = enabled,
                onPressed = onPressed,
                onClick = onClick,
            )
            .padding(padding)
            .then(if (keyboardInteractionEnabled) Modifier.imePadding() else Modifier),
        shape = shapeByKeyboardShow,
        color = backgroundColor,
        content = content,
    )
}

@Composable
private fun ColoredButton(
    modifier: Modifier,
    color: ButtonColor,
    shape: RoundedCornerShape,
    enabled: Boolean,
    buttonType: ButtonType,
    keyboardInteractionEnabled: Boolean,
    pressed: () -> Boolean,
    onPressed: (pressed: Boolean) -> Unit,
    onClick: () -> Unit,
    content: @Composable (contentColor: Color) -> Unit,
) {
    val themeColor = getThemeColor(color = color)

    val background by animateColorAsState(
        targetValue = if (!enabled) {
            JobisTheme.colors.surfaceTint
        } else if (pressed()) {
            themeColor.pressed
        } else {
            themeColor.background
        },
        label = "",
    )

    val contentColor by animateColorAsState(
        targetValue = if (!enabled) {
            JobisTheme.colors.background
        } else {
            themeColor.text
        },
        animationSpec = tween(durationMillis = DURATION_MILLIS),
        label = "",
    )

    BasicButton(
        modifier = modifier,
        backgroundColor = background,
        shape = shape,
        enabled = enabled,
        buttonType = buttonType,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        onPressed = onPressed,
        onClick = onClick,
        content = { content(contentColor) },
    )
}

@Composable
private fun getThemeColor(color: ButtonColor) = when (color) {
    ButtonColor.Primary -> checkDarkTheme(
        lightColor = ButtonColors.Light.primary(),
        darkColor = ButtonColors.Dark.primary(),
    )

    ButtonColor.Secondary -> checkDarkTheme(
        lightColor = ButtonColors.Light.secondary(),
        darkColor = ButtonColors.Dark.secondary(),
    )

    ButtonColor.Default -> checkDarkTheme(
        lightColor = ButtonColors.Light.default(),
        darkColor = ButtonColors.Dark.default(),
    )

    ButtonColor.Error -> checkDarkTheme(
        lightColor = ButtonColors.Light.error(),
        darkColor = ButtonColors.Dark.error(),
    )
}

@Composable
private fun checkDarkTheme(
    lightColor: ButtonColorSet,
    darkColor: ButtonColorSet,
): ButtonColorSet {
    return if (isSystemInDarkTheme()) {
        darkColor
    } else {
        lightColor
    }
}

@Composable
private fun LargeButton(
    modifier: Modifier,
    text: String,
    color: ButtonColor,
    enabled: Boolean,
    buttonType: ButtonType,
    keyboardInteractionEnabled: Boolean,
    onClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }

    ColoredButton(
        modifier = modifier.fillMaxWidth(),
        color = color,
        shape = largeButtonShape,
        enabled = enabled,
        buttonType = buttonType,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        pressed = { pressed },
        onPressed = { pressed = it },
        onClick = onClick,
    ) { contentColor ->
        Row(
            modifier = Modifier.padding(
                start = 28.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            JobisText(
                text = text,
                style = JobisTypography.SubHeadLine,
                color = contentColor,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = JobisIcon.LongArrow),
                tint = contentColor,
                contentDescription = stringResource(id = R.string.content_description_long_arrow),
            )
        }
    }
}

@Composable
private fun SmallButton(
    modifier: Modifier,
    text: String,
    color: ButtonColor,
    enabled: Boolean,
    keyboardInteractionEnabled: Boolean,
    onClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }

    ColoredButton(
        modifier = modifier,
        color = color,
        shape = smallButtonShape,
        enabled = enabled,
        buttonType = ButtonType.SMALL,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        pressed = { pressed },
        onPressed = { pressed = it },
        onClick = onClick,
    ) { contentColor ->
        JobisText(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
            text = text,
            style = JobisTypography.SubBody,
            color = contentColor,
        )
    }
}

@Composable
private fun MediumButton(
    modifier: Modifier,
    text: String,
    color: ButtonColor,
    painter: Painter,
    enabled: Boolean,
    keyboardInteractionEnabled: Boolean,
    onClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }

    ColoredButton(
        modifier = modifier,
        color = color,
        shape = mediumButtonShape,
        enabled = enabled,
        buttonType = ButtonType.SMALL,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        pressed = { pressed },
        onPressed = { pressed = it },
        onClick = onClick,
    ) { contentColor ->
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter = painter,
                tint = contentColor,
                contentDescription = "drawable",
            )
            JobisText(
                text = text,
                style = JobisTypography.Body,
                color = contentColor,
            )
        }
    }
}

/**
 * This composable function creates a JobisButton element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisButton.
 * @param text Text to be written on the button
 * @param color To color inside this button
 * @param enabled Controls the enabled state.
 * @param keyboardInteractionEnabled Determines whether the button interacts with the keyboard or not
 * @param onClick Called when this button is clicked
 */
@Composable
fun JobisButton(
    modifier: Modifier = Modifier,
    text: String,
    color: ButtonColor = ButtonColor.Default,
    enabled: Boolean = true,
    keyboardInteractionEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    LargeButton(
        modifier = modifier,
        text = text,
        color = color,
        enabled = enabled,
        buttonType = ButtonType.LARGE,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        onClick = onClick,
    )
}

@Composable
fun JobisSmallButton(
    modifier: Modifier = Modifier,
    text: String,
    color: ButtonColor = ButtonColor.Secondary,
    enabled: Boolean = true,
    keyboardInteractionEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    SmallButton(
        modifier = modifier,
        text = text,
        color = color,
        enabled = enabled,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        onClick = onClick,
    )
}

@Composable
fun JobisMediumButton(
    modifier: Modifier = Modifier,
    text: String,
    color: ButtonColor = ButtonColor.Default,
    drawable: Painter,
    enabled: Boolean = true,
    keyboardInteractionEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    MediumButton(
        modifier = modifier,
        text = text,
        color = color,
        painter = drawable,
        enabled = enabled,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        onClick = onClick,
    )
}

@Composable
fun JobisDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    color: ButtonColor = ButtonColor.Default,
    enabled: Boolean = true,
    keyboardInteractionEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    LargeButton(
        modifier = modifier,
        text = text,
        color = color,
        enabled = enabled,
        buttonType = ButtonType.DIALOG,
        keyboardInteractionEnabled = keyboardInteractionEnabled,
        onClick = onClick,
    )
}
