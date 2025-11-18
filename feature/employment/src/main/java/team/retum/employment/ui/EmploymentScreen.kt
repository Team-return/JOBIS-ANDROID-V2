package team.retum.employment.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.employment.R
import team.retum.employment.viewmodel.EmploymentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
internal fun Employment(
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
    employmentViewModel: EmploymentViewModel = hiltViewModel(),
) {
    val state by employmentViewModel.state.collectAsStateWithLifecycle()
    val animatedValue = remember { Animatable(state.rate) }

    LaunchedEffect(state.rate) {
        animatedValue.animateTo(
            targetValue = state.rate,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        )
    }

    LaunchedEffect(Unit) {
        with(employmentViewModel) {
            fetchEmploymentCount()
        }
    }

    EmploymentScreen(
        onBackPressed = onBackPressed,
        onClassClick = onClassClick,
        rate = animatedValue.value,
        totalStudentCount = state.totalStudentCount,
        passCount = state.passCount,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmploymentScreen(
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
    rate: Float,
    totalStudentCount: String,
    passCount: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.employment_status),
            onBackPressed = onBackPressed,
        ) {
            JobisIconButton(
                drawableResId = JobisIcon.Filter,
                contentDescription = "filter",
                onClick = {  },
                tint = JobisTheme.colors.onPrimary,
            )
        }
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
            EmploymentRate(
                passCount = passCount,
                totalStudentCount = totalStudentCount,
                rate = rate,
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
        ) {
            JobisText(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = stringResource(id = R.string.check_employment_status),
                style = JobisTypography.Body,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 2,
        ) {
            ClassEmploymentButton(
                modifier = Modifier.weight(1f),
                onClassClick = { onClassClick(1) },
                image = team.retum.design_system.R.drawable.ic_computer,
                text = stringResource(R.string.first_class),
            )
            ClassEmploymentButton(
                modifier = Modifier.weight(1f),
                onClassClick = { onClassClick(2) },
                image = team.retum.design_system.R.drawable.ic_computer,
                text = stringResource(R.string.second_class),
            )
            ClassEmploymentButton(
                modifier = Modifier
                    .weight(1f),
                onClassClick = { onClassClick(3) },
                image = team.retum.design_system.R.drawable.ic_spanner,
                text = stringResource(R.string.third_class),
            )
            ClassEmploymentButton(
                modifier = Modifier
                    .weight(1f),
                onClassClick = { onClassClick(4) },
                image = team.retum.design_system.R.drawable.ic_robot,
                text = stringResource(R.string.fourth_class),
            )
        }
    }
}

@Composable
private fun EmploymentRate(
    rate: Float,
    totalStudentCount: String,
    passCount: String,
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
        Row {
            JobisText(
                text = stringResource(id = R.string.employment_status),
                style = JobisTypography.SubBody,
                color = JobisTheme.colors.onPrimary,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
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
                                color = JobisTheme.colors.onSecondary,
                                shape = CircleShape,
                            )
                            .align(Alignment.CenterVertically),
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    JobisText(
                        text = stringResource(id = R.string.before_employment),
                        style = JobisTypography.Caption,
                        textAlign = TextAlign.End,
                        color = JobisTheme.colors.onSecondary,
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
}

@Composable
private fun CircleProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    radius: Dp = 120.dp,
    strokeWidth: Dp = 24.dp,
    primaryColor: Color = JobisTheme.colors.onSecondary,
    secondaryColor: Color = JobisTheme.colors.onPrimary,
) {
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
                sweepAngle = percentage * 3.6f,
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
            text = "${percentage.toInt()}%",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onPrimary,
        )
    }
}

@Composable
private fun ClassEmploymentButton(
    modifier: Modifier = Modifier,
    onClassClick: (Int) -> Unit,
    image: Int,
    text: String,
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f),
        onClick = { onClassClick(4) },
        color = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(JobisTheme.colors.background)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(0.7f),
                    painter = painterResource(image),
                    contentDescription = text,
                    tint = Color.Unspecified,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(color = JobisTheme.colors.surfaceTint, shape = RoundedCornerShape(12.dp))
                    .align(Alignment.Start),
            ) {
                JobisText(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = text,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.background,
                )
            }
        }
    }
}
