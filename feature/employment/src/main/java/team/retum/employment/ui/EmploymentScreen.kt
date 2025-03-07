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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.employment.viewmodel.EmploymentViewModel
import team.retum.jobis.employment.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
fun Employment(
    onBackPressed: () -> Unit,
    onClassClick: () -> Unit,
    rate: Float,
    totalStudentCount: Long,
    passCount: Long,
    employmentViewModel: EmploymentViewModel = hiltViewModel(),
) {
    val animatedValue = remember { Animatable(rate) }
    LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = rate,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        )
        with(employmentViewModel) {
            fetchEmploymentCount()
        }
    }

    EmploymentScreen(
        onBackPressed = onBackPressed,
        onClassClick = onClassClick,
        rate = rate,
        totalStudentCount = totalStudentCount,
        passCount = passCount,
    )
}

@Composable
fun EmploymentScreen(
    onBackPressed: () -> Unit,
    onClassClick: () -> Unit,
    rate: Float,
    totalStudentCount: Long,
    passCount: Long,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
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
                .background(JobisTheme.colors.inverseSurface),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
            ) {
                EmploymentCard(
                    passCount = passCount,
                    totalStudentCount = totalStudentCount,
                    rate = rate,
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
        ) {
            JobisText(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = stringResource(id = R.string.check_employment_status),
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ClassEmploymentButton(
                    onClassClick = onClassClick,
                    image = team.retum.design_system.R.drawable.ic_computer,
                    text = "1반",
                )
                ClassEmploymentButton(
                    onClassClick = onClassClick,
                    image = team.retum.design_system.R.drawable.ic_computer,
                    text = "2반",
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ClassEmploymentButton(
                    onClassClick = onClassClick,
                    image = team.retum.design_system.R.drawable.ic_spanner,
                    text = "3반",
                )
                ClassEmploymentButton(
                    onClassClick = onClassClick,
                    image = team.retum.design_system.R.drawable.ic_robot,
                    text = "4반",
                )
            }
        }
    }
}

@Composable
fun EmploymentCard(
    rate: Float,
    totalStudentCount: Long,
    passCount: Long,
) {
    Row {
        JobisText(
            text = stringResource(id = R.string.employment_status),
            style = JobisTypography.SubBody,
            color = JobisTheme.colors.onPrimary,
            textAlign = TextAlign.Start,
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
                        .align(Alignment.CenterVertically),
                )
                Spacer(modifier = Modifier.size(6.dp))
                JobisText(
                    text = stringResource(id = R.string.employment_completed),
                    style = JobisTypography.Caption,
                    textAlign = TextAlign.End,
                    color = JobisTheme.colors.onPrimary,
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
                        .align(Alignment.CenterVertically),
                )
                Spacer(modifier = Modifier.size(6.dp))
                JobisText(
                    text = stringResource(id = R.string.before_employment),
                    style = JobisTypography.Caption,
                    textAlign = TextAlign.End,
                    color = JobisTheme.colors.onSurfaceVariant,
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
        CircleProgress(percentage = rate)
        Spacer(
            modifier = Modifier
                .padding(start = 16.dp, end = 20.dp)
                .width(1.dp)
                .height(30.dp)
                .background(JobisTheme.colors.surfaceVariant),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            JobisText(
                text = stringResource(R.string.all_statistics),
                style = JobisTypography.Body,
                color = JobisTheme.colors.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.height(8.dp))
            JobisText(
                text = "$passCount/$totalStudentCount",
                style = JobisTypography.HeadLine,
                color = JobisTheme.colors.onPrimary,
            )
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
    secondaryColor: Color = JobisTheme.colors.onPrimary,
) {
    val animatedValue = remember { Animatable(percentage) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius),
    ) {
        Canvas(
            modifier = Modifier
                .size(radius)
                .scale(scaleX = -1f, scaleY = 1f),
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
                    (size.height - arcRadius * 2) / 2,
                ),
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
                    (size.height - arcRadius * 2) / 2,
                ),
            )
        }

        JobisText(
            text = "${animatedValue.value.toInt()}%",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onPrimary,
        )
    }
}

@Composable
fun ClassEmploymentButton(
    onClassClick: () -> Unit,
    @DrawableRes image: Int,
    description: String = "",
    text: String,
) {
    Surface(
        modifier = Modifier.padding(),
        onClick = onClassClick,
        color = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.background(color = JobisTheme.colors.inverseSurface),
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 34.dp, end = 34.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(JobisTheme.colors.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp),
                        painter = painterResource(image),
                        contentDescription = description,
                        tint = Color.Unspecified,
                    )
                }
            }
            Surface(
                color = JobisTheme.colors.surfaceTint,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 16.dp, start = 12.dp, bottom = 12.dp),
            ) {
                JobisText(
                    text = text,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.background,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}
