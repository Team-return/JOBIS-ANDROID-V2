package team.retum.jobisdesignsystemv2.textfield

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

/**
 * This class represents what the DescriptionType is.
 *
 * @property icon Icon of each description
 * @property contentDescription type of description
 * @property tint To color inside this icon
 * @property color To color inside this description
 */
sealed class DescriptionType(
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int,
    val tint: @Composable () -> Color,
    val color: @Composable () -> Color,
) {
    data object Check : DescriptionType(
        icon = JobisIcon.CheckCircle,
        contentDescription = R.string.content_description_check_circle,
        tint = { JobisTheme.colors.outlineVariant },
        color = { JobisTheme.colors.onBackground },
    )

    data object Error : DescriptionType(
        icon = JobisIcon.Error,
        contentDescription = R.string.content_description_error,
        tint = { JobisTheme.colors.error },
        color = { JobisTheme.colors.error },
    )

    data object Information : DescriptionType(
        icon = JobisIcon.Information,
        contentDescription = R.string.content_description_information,
        tint = { JobisTheme.colors.onPrimary },
        color = { JobisTheme.colors.onBackground },
    )
}

@Composable
private fun TextFieldTitle(
    title: String,
    style: TextStyle,
    color: Color,
) {
    JobisText(
        modifier = Modifier.padding(bottom = 4.dp),
        text = title,
        style = style,
        color = color,
    )
}

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    style: TextStyle,
    value: () -> String,
    hint: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    maxLength: Int,
    showEmailHint: Boolean,
    showVisibleIcon: Boolean,
    leadingIcon: Painter?,
    content: @Composable () -> Unit,
) {
    val hintAlpha by animateFloatAsState(
        targetValue = if (value().isEmpty()) {
            1f
        } else {
            0f
        },
        label = "",
    )
    var visible by remember { mutableStateOf(false) }
    val (visualTransformation, icon) = if (visible || !showVisibleIcon) {
        VisualTransformation.None to JobisIcon.EyeOn
    } else {
        PasswordVisualTransformation() to JobisIcon.EyeOff
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = JobisTheme.colors.inverseSurface,
    ) {
        BasicTextField(
            value = value().take(maxLength),
            onValueChange = onValueChange,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
            textStyle = style,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            cursorBrush = SolidColor(JobisTheme.colors.onBackground),
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.run {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = "leading icon",
                        tint = JobisTheme.colors.onSurfaceVariant,
                    )
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    innerTextField()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        JobisText(
                            modifier = Modifier.alpha(hintAlpha),
                            text = hint,
                            style = style,
                            color = JobisTheme.colors.onSurfaceVariant,
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showEmailHint) {
                        JobisText(
                            modifier = Modifier
                                .padding(
                                    end = 8.dp,
                                    bottom = 1.dp,
                                )
                                .align(Alignment.CenterVertically),
                            text = "@dsm.hs.kr",
                            style = style,
                            color = JobisTheme.colors.onSurfaceVariant,
                        )
                    }
                    content()
                    if (showVisibleIcon) {
                        JobisIconButton(
                            painter = painterResource(id = icon),
                            contentDescription = stringResource(id = R.string.content_description_eye_off),
                            onClick = { visible = !visible },
                            defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Description(
    modifier: Modifier = Modifier,
    errorDescription: String,
    checkDescription: String,
    informationDescription: String,
    descriptionType: DescriptionType,
) {
    val description = when (descriptionType) {
        DescriptionType.Check -> checkDescription
        DescriptionType.Error -> errorDescription
        else -> informationDescription
    }

    Crossfade(
        targetState = descriptionType,
        animationSpec = tween(durationMillis = 300),
        label = "",
    ) { targetDescriptionType ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = targetDescriptionType.icon),
                contentDescription = stringResource(id = targetDescriptionType.contentDescription),
                tint = targetDescriptionType.tint(),
            )
            JobisText(
                text = description,
                style = JobisTypography.Description,
                color = targetDescriptionType.color(),
            )
        }
    }
}

/**
 *  This composable function creates a JobisTextField element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisTextField.
 * @param title Title of text field
 * @param value The input String text to be shown in the text field
 * @param hint The hint text to be displayed when the text field is empty.
 * @param onValueChange A lambda function to be invoked when the text field value changes.
 * @param showDescription A lambda function that returns a boolean indicating whether to show the description.
 * @param errorDescription When the input value does not match the regular expression
 * @param checkDescription When the input value matches a regular expression
 * @param informationDescription If there is an explanation about regular expressions
 * @param descriptionType Description for the current value The default is Information.
 * @param titleStyle The [TextStyle] to be applied to the title.
 * @param titleColor The color of the title text.
 * @param style The [TextStyle] to be applied to the text field.
 * @param singleLine Whether the text field should be a single line or multiline.
 * @param showEmailHint Whether to show email-specific features (e.g., @dsm.hs.kr).
 */
@Composable
fun JobisTextField(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: () -> String,
    hint: String,
    onValueChange: (String) -> Unit,
    showDescription: () -> Boolean = { false },
    errorDescription: String = "",
    checkDescription: String = "",
    informationDescription: String = "",
    descriptionType: DescriptionType = DescriptionType.Information,
    titleStyle: TextStyle = JobisTypography.Description,
    titleColor: Color = JobisTheme.colors.onSurface,
    style: TextStyle = JobisTypography.Body,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    showEmailHint: Boolean = false,
    showVisibleIcon: Boolean = false,
    leadingIcon: Painter? = null,
    content: @Composable () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        title?.run {
            TextFieldTitle(
                title = this,
                style = titleStyle,
                color = titleColor,
            )
        }
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            style = style,
            value = value,
            hint = hint,
            onValueChange = onValueChange,
            singleLine = singleLine,
            imeAction = imeAction,
            keyboardType = keyboardType,
            maxLength = maxLength,
            showEmailHint = showEmailHint,
            showVisibleIcon = showVisibleIcon,
            leadingIcon = leadingIcon,
            content = content,
        )
        AnimatedVisibility(
            visible = showDescription(),
            enter = expandVertically(tween(300)) + fadeIn(tween(300)),
            exit = shrinkVertically(tween(300)) + fadeOut(tween(300)),
        ) {
            Description(
                errorDescription = errorDescription,
                checkDescription = checkDescription,
                informationDescription = informationDescription,
                descriptionType = descriptionType,
            )
        }
    }
}
