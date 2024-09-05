package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.company.model.CompanyInfoData
import team.retum.company.viewmodel.CompanyDetailsSideEffect
import team.retum.company.viewmodel.CompanyDetailsState
import team.retum.company.viewmodel.CompanyDetailsViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.review.ReviewContent
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.FetchReviewsEntity

@Composable
internal fun CompanyDetails(
    companyId: Long,
    onBackPressed: () -> Unit,
    navigateToReviewDetails: (String, String) -> Unit,
    navigateToReviews: (Long, String) -> Unit,
    navigateToRecruitmentDetails: (Long, Boolean) -> Unit,
    isMovedRecruitmentDetails: Boolean,
    companyDetailsViewModel: CompanyDetailsViewModel = hiltViewModel(),
) {
    val state by companyDetailsViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(companyDetailsViewModel) {
            setCompanyId(companyId = companyId)
            fetchCompanyDetails()
            fetchReviews()
        }

        companyDetailsViewModel.sideEffect.collect {
            when (it) {
                is CompanyDetailsSideEffect.FetchCompanyDetailsError -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.toast_error_fetch_company_details),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is CompanyDetailsSideEffect.MoveToRecruitmentDetails -> {
                    navigateToRecruitmentDetails(it.recruitmentId, true)
                }
            }
        }
    }

    val companyInfo = with(state.companyDetailsEntity) {
        listOf(
            CompanyInfoData(R.string.representative_name, representativeName),
            CompanyInfoData(R.string.founded_at, foundedAt),
            CompanyInfoData(R.string.worker_number, workerNumber),
            CompanyInfoData(R.string.take, take),
            CompanyInfoData(R.string.main_address, mainAddress),
            CompanyInfoData(R.string.sub_address, subAddress),
            CompanyInfoData(R.string.manager, managerName),
            CompanyInfoData(R.string.manager_phone_number, managerPhoneNo),
            CompanyInfoData(R.string.sub_manager, subManagerName),
            CompanyInfoData(R.string.sub_manager_phone_number, subManagerPhoneNo),
            CompanyInfoData(R.string.email, email),
            CompanyInfoData(R.string.fax, fax),
        )
    }

    CompanyDetailsScreen(
        companyId = companyId,
        onBackPressed = onBackPressed,
        navigateToReviewDetails = navigateToReviewDetails,
        navigateToReviews = navigateToReviews,
        onMoveToRecruitmentButtonClick = companyDetailsViewModel::onMoveToRecruitmentButtonClick,
        isMovedRecruitmentDetails = isMovedRecruitmentDetails,
        state = state,
        companyInfo = companyInfo.toPersistentList(),
    )
}

@Composable
private fun CompanyDetailsScreen(
    companyId: Long,
    onBackPressed: () -> Unit,
    navigateToReviewDetails: (String, String) -> Unit,
    navigateToReviews: (Long, String) -> Unit,
    onMoveToRecruitmentButtonClick: () -> Unit,
    isMovedRecruitmentDetails: Boolean,
    state: CompanyDetailsState,
    companyInfo: ImmutableList<CompanyInfoData>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.company_details),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            CompanyMainInformation(
                companyLogoUrl = state.companyDetailsEntity.companyProfileUrl,
                name = state.companyDetailsEntity.companyName,
                description = state.companyDetailsEntity.companyIntroduce,
            )
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                companyInfo.forEach { (title, detail) ->
                    CompanyInformation(
                        title = stringResource(id = title),
                        detail = detail,
                    )
                }
            }
            if (state.reviews.isNotEmpty()) {
                Reviews(
                    reviews = state.reviews.toPersistentList(),
                    navigateToReviewDetails = navigateToReviewDetails,
                    showMoreReviews = state.showMoreReviews,
                    onShowMoreReviewClick = {
                        navigateToReviews(
                            companyId,
                            state.companyDetailsEntity.companyName,
                        )
                    },
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.showMoveToRecruitmentButton) {
                JobisButton(
                    text = stringResource(id = R.string.show_recruitment),
                    onClick = onMoveToRecruitmentButtonClick,
                    color = ButtonColor.Primary,
                    enabled = state.buttonEnabled && !isMovedRecruitmentDetails,
                )
            }
        }
    }
}

@Composable
private fun CompanyMainInformation(
    companyLogoUrl: String,
    name: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = JobisTheme.colors.inverseSurface,
                        shape = RoundedCornerShape(8.dp),
                    ),
                model = companyLogoUrl,
                contentDescription = "company logo",
                contentScale = ContentScale.Crop,
            )
            JobisText(
                text = name,
                style = JobisTypography.HeadLine,
            )
        }
        JobisText(
            text = description,
            style = JobisTypography.Body,
        )
    }
}

@Composable
private fun CompanyInformation(
    title: String,
    detail: String?,
) {
    if (!detail.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            JobisText(
                text = title,
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
            JobisText(
                text = detail,
                style = JobisTypography.SubBody,
                color = JobisTheme.colors.onSurface,
            )
        }
    }
}

@Composable
private fun Reviews(
    reviews: ImmutableList<FetchReviewsEntity.Review>,
    navigateToReviewDetails: (String, String) -> Unit,
    showMoreReviews: Boolean,
    onShowMoreReviewClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.review),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            reviews.forEach {
                ReviewContent(
                    onClick = navigateToReviewDetails,
                    reviewId = it.reviewId,
                    writer = it.writer,
                    year = it.year,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (showMoreReviews) {
            JobisText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onShowMoreReviewClick),
                text = stringResource(id = R.string.show_more_reviews),
                style = JobisTypography.SubBody,
                color = JobisTheme.colors.onSurfaceVariant,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}
