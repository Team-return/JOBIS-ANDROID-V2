package team.retum.employment.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import team.retum.employment.R
import team.retum.employment.navigation.NAVIGATION_EMPLOYMENT
import team.retum.employment.viewmodel.EmploymentSideEffect
import team.retum.employment.viewmodel.EmploymentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
internal fun Employment(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
    onFilterClick: () -> Unit,
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(NAVIGATION_EMPLOYMENT)
    }
    val employmentViewModel: EmploymentViewModel = hiltViewModel(parentEntry)
    val context = LocalContext.current
    val state by employmentViewModel.state.collectAsStateWithLifecycle()
    val animatedValue = remember { Animatable(state.rate) }

    LaunchedEffect(state.rate) {
        animatedValue.animateTo(
            targetValue = state.rate,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        )
    }

    LaunchedEffect(state.selectedYear) {
        employmentViewModel.fetchEmploymentCount(state.selectedYear)
    }

    LaunchedEffect(Unit) {
        employmentViewModel.sideEffect.collect {
            when (it) {
                is EmploymentSideEffect.FetchEmploymentCountError -> JobisToast.create(
                    context = context,
                    message = context.getString(R.string.toast_fetch_employment_count_error),
                    drawable = JobisIcon.Error,
                ).show()
            }
        }
    }

    EmploymentScreen(
        onBackPressed = onBackPressed,
        onClassClick = onClassClick,
        onFilterClick = onFilterClick,
        rate = animatedValue.value,
        totalStudentCount = state.totalStudentCount,
        passCount = state.passCount,
        year = state.selectedYear,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmploymentScreen(
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
    onFilterClick: () -> Unit,
    rate: Float,
    totalStudentCount: Long,
    passCount: Long,
    year: Int,
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
                onClick = onFilterClick,
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
                year = "$year",
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
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ClassEmploymentButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClassClick = { onClassClick(1) },
                        image = team.retum.design_system.R.drawable.ic_computer,
                        text = stringResource(R.string.first_class),
                    )
                    ClassEmploymentButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClassClick = { onClassClick(2) },
                        image = team.retum.design_system.R.drawable.ic_computer,
                        text = stringResource(R.string.second_class),
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ClassEmploymentButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClassClick = { onClassClick(3) },
                        image = team.retum.design_system.R.drawable.ic_spanner,
                        text = stringResource(R.string.third_class),
                    )
                    ClassEmploymentButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClassClick = { onClassClick(4) },
                        image = team.retum.design_system.R.drawable.ic_robot,
                        text = stringResource(R.string.fourth_class),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmploymentRate(
    rate: Float,
    totalStudentCount: Long,
    passCount: Long,
    year: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                start = 16.dp,
                end = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                .padding(top = 34.dp, bottom = 66.dp),
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
        JobisText(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "$year 취업률",
            textAlign = TextAlign.Center,
            style = JobisTypography.SubBody,
            color = JobisTheme.colors.onSurfaceVariant,
        )
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
            text = "%.1f%%".format(percentage),
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
        modifier = modifier,
        onClick = { onClassClick(4) },
        color = JobisTheme.colors.inverseSurface,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(JobisTheme.colors.background, shape = CircleShape)
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize(0.3f),
                    painter = painterResource(image),
                    contentDescription = text,
                    tint = Color.Unspecified,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(start = 12.dp, bottom = 12.dp)
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
