package team.retum.home.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import team.retum.common.enums.ApplyStatus
import team.retum.home.R
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.application.AppliedCompaniesEntity

private const val MAX_LENGTH_COMPANY_NAME = 10

@Composable
internal fun ApplyCompanyItem(
    onShowRejectionReasonClick: () -> Unit,
    appliedCompany: AppliedCompaniesEntity.ApplicationEntity,
    isFocus: Boolean,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigateToApplication: () -> Unit,
) {
    val applicationStatus = remember { appliedCompany.applicationStatus }
    val color = when (applicationStatus) {
        ApplyStatus.FAILED, ApplyStatus.REJECTED -> JobisTheme.colors.error
        ApplyStatus.REQUESTED, ApplyStatus.APPROVED -> JobisTheme.colors.tertiary
        ApplyStatus.FIELD_TRAIN, ApplyStatus.ACCEPTANCE, ApplyStatus.PASS -> JobisTheme.colors.outlineVariant
        else -> JobisTheme.colors.onPrimary
    }
    var effectExecuted by remember { mutableStateOf(isFocus) }
    val animationAlpha by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(1),
        ),
        label = "",
    )
    val alpha by animateFloatAsState(
        targetValue = if (effectExecuted) {
            animationAlpha
        } else {
            0f
        },
        label = "",
    )

    if (isFocus) {
        LaunchedEffect(Unit) {
            delay(1000)
            effectExecuted = false
        }
    }

    JobisCard(
        onClick = {
            when (applicationStatus) {
                ApplyStatus.REJECTED -> onShowRejectionReasonClick()
                ApplyStatus.REQUESTED -> navigateToApplication()
                else -> navigateToRecruitmentDetails(appliedCompany.recruitmentId)
            }
        },
    ) {
        Row(
            modifier = Modifier
                .background(color = JobisTheme.colors.surfaceTint.copy(alpha = alpha))
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(JobisTheme.colors.background),
                model = appliedCompany.companyLogoUrl,
                contentDescription = "company profile url",
            )
            Spacer(modifier = Modifier.width(8.dp))
            JobisText(
                modifier = Modifier.weight(1f),
                text = appliedCompany.company.toOverFlowText(MAX_LENGTH_COMPANY_NAME),
                style = JobisTypography.Body,
            )
            JobisText(
                text = applicationStatus.value,
                style = JobisTypography.SubBody,
                color = color,
            )
            if (applicationStatus == ApplyStatus.REJECTED || applicationStatus == ApplyStatus.REQUESTED) {
                JobisText(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(
                        id = when (applicationStatus) {
                            ApplyStatus.REJECTED -> R.string.reason_rejection
                            else -> R.string.re_apply
                        },
                    ).plus("->"),
                    style = JobisTypography.SubBody,
                    color = color,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    }
}

private fun String.toOverFlowText(take: Int): String {
    return this.take(take).run {
        if (this@toOverFlowText.length > MAX_LENGTH_COMPANY_NAME) {
            this@run.plus("...")
        } else {
            this
        }
    }
}
