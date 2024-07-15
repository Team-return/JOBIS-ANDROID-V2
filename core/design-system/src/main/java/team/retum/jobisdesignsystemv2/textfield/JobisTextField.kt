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
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
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
 * [JobisTextField]에서 표시되는 description의 종류를 결정
 *
 * @property icon description에 포함될 아이콘
 * @property contentDescription 표시할 string resource id
 * @property tint description과 함께 표시될 아이콘의 색상
 * @property color description의 색상
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
    fieldColor: Color,
    testTag: String,
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
            modifier = Modifier
                .background(fieldColor)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .testTag(testTag),
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
 *  JOBIS에서 사용하는 커스텀 텍스트 필드
 *
 * @param modifier [JobisTextField]에 적용될 [Modifier]
 * @param title 텍스트 필드의 제목
 * @param value 사용자에게 보여질 텍스트 필드의 값
 * @param hint 텍스트 필드 값이 비어있을 때 사용자에게 보여질 값
 * @param onValueChange 텍스트 필드의 값이 변경될 때 호출될 함수
 * @param showDescription description의 표시 여부를 반환하는 함수
 * @param errorDescription [DescriptionType.Error]일 때 보여질 description
 * @param checkDescription [DescriptionType.Check]일 때 보여질 description
 * @param informationDescription [DescriptionType.Information]일 때 보여질 description
 * @param descriptionType 텍스트 필드에 보여질 description 종류
 * @param titleStyle 텍스트 필드 제목에 적용될 [TextStyle]
 * @param titleColor 텍스트 필드 제목에 적용될 [Color]
 * @param style 텍스트 필드의 값에 적용될 [TextStyle]
 * @param imeAction 키보드에 적용될 [ImeAction]
 * @param keyboardType 키보드 입력 종류
 * @param singleLine 텍스트 필드에 다중 줄을 허용할 것인지 결정
 * @param maxLength 텍스트 필드 값의 최대 길이
 * @param showEmailHint 이메일 도메인에 대한 힌트를 표시할 것인지 결정(e.g., @dsm.hs.kr).
 * @param showVisibleIcon 비밀번호 표시
 * @param leadingIcon
 * @param fieldColor 배경 색상
 * @param testTag UI 테스트 코드에서 TextField 노드를 찾기 위해 사용할 태그
 * @param content 내부에 추가로 표시될 뷰
 * 다음과 같이 사용할 수 있다.
 * ```
 * JobisTextField(
 *     title = stringResource(id = R.string.authentication_code),
 *     value = authenticationCode,
 *     hint = stringResource(id = R.string.hint_authentication_code),
 *     onValueChange = onAuthenticationCodeChange,
 *     errorDescription = stringResource(id = R.string.description),
 *     showDescription = showAuthenticationCodeDescription,
 *     descriptionType = DescriptionType.Error,
 *     keyboardType = KeyboardType.NumberPassword,
 * ) {
 *     JobisText(
 *         text = remainTime,
 *         style = JobisTypography.Body,
 *         color = JobisTheme.colors.onSurfaceVariant,
 *     )
 * }
 * ```
 * 위의 코드 예시는 [JobisTextField] 내부에 이메일 인증 코드 유효 시간을 표시하는 코드이다.
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
    fieldColor: Color = JobisTheme.colors.inverseSurface,
    testTag: String = "",
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
            fieldColor = fieldColor,
            testTag = testTag,
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
