package team.retum.employment.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import team.retum.employment.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
fun Employment(percentage: Float) { // TODO :: 네비게이션 추가 및 부수

    val animatedValue = remember { Animatable(percentage) }
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }
}

@Composable
fun EmploymentScreen(
    onBackPressed: () -> Unit,
) { // TODO :: UI 퍼블리싱
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.employment_status),
            onBackPressed = onBackPressed,
        )
        JobisCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                )
                .clip(RoundedCornerShape(12.dp))
                .background(JobisTheme.colors.inverseSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
            ) {
                Row {
                    JobisText(
                        text = stringResource(id = R.string.employment_status),
                        style = JobisTypography.SubBody,
                        color = JobisTheme.colors.onPrimary,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(0.78f))
                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(space = 4.dp),
                    ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = JobisTheme.colors.onPrimary,
                                        shape = CircleShape,
                                    )
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            JobisText(
                                text = stringResource(id = R.string.employment_completed),
                                style = JobisTypography.Caption,
                                textAlign = TextAlign.End,
                                color = JobisTheme.colors.onPrimary
                            )
                        }
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = JobisTheme.colors.secondaryContainer,
                                        shape = CircleShape,
                                    )
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            JobisText(
                                text = stringResource(id = R.string.before_employment),
                                style = JobisTypography.Caption,
                                textAlign = TextAlign.End,
                                color = JobisTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 34.dp, bottom = 82.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircleProgress(80f)
                    Spacer(modifier = Modifier.padding(start = 16.dp, end = 20.dp).width(1.dp).height(30.dp).background(JobisTheme.colors.error))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        JobisText(
                            text = "전체 통계",
                            style = JobisTypography.Body,
                            color = JobisTheme.colors.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        JobisText(
                            text = "59/64명",
                            style = JobisTypography.HeadLine,
                            color = JobisTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircleProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    radius: Dp = 120.dp,
    strokeWidth: Dp = 25.dp,
    primaryColor: Color = JobisTheme.colors.secondaryContainer,
    secondaryColor: Color = JobisTheme.colors.onPrimary
) {
    val animatedValue = remember { Animatable(percentage) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius)
    ) {
        Canvas(
            modifier = Modifier
                .size(radius)
                .scale(scaleX = -1f, scaleY = 1f)
        ) {
            val canvasSize = size.minDimension
            val arcRadius = canvasSize / 2 - strokeWidth.toPx() / 2

            drawArc(
                color = primaryColor,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(arcRadius * 2, arcRadius * 2),
                topLeft = Offset(
                    (size.width - arcRadius * 2) / 2,
                    (size.height - arcRadius * 2) / 2
                )
            )

            drawArc(
                color = secondaryColor,
                startAngle = 270f,
                sweepAngle = animatedValue.value * 3.6f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt),
                size = Size(arcRadius * 2, arcRadius * 2),
                topLeft = Offset(
                    (size.width - arcRadius * 2) / 2,
                    (size.height - arcRadius * 2) / 2
                )
            )
        }

        JobisText(
            text = "${animatedValue.value.toInt()}%",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onPrimary
        )
    }
}

@Composable
fun ClassEmploymentButton(
    onClassClick: () -> Unit,
    @DrawableRes image: Int,
    description: String,
    text: String,
) {
    Surface (
        onClick = onClassClick,
        color = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 34.dp, end = 34.dp, top = 20.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(1000.dp)),
                painter = painterResource(image),
                contentDescription = description,
                tint = Color.Unspecified
            )
            Surface(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 16.dp, start = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = JobisTheme.colors.onSurfaceVariant,
            ) {
                JobisText(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    text = text,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.background
                )
            }
        }
    }
}

@Preview
@Composable
fun EmploymentPreview() {
    JobisDesignSystemV2Theme {
        EmploymentScreen(onBackPressed = {})
    }
}
