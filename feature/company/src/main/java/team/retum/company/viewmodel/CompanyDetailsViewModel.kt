package team.retum.company.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.entity.company.CompanyDetailsEntity
import team.retum.usecase.usecase.company.FetchCompanyDetailsUseCase
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import javax.inject.Inject

private const val MAX_REVIEW_COUNT = 3

@HiltViewModel
internal class CompanyDetailsViewModel @Inject constructor(
    private val fetchCompanyDetailsUseCase: FetchCompanyDetailsUseCase,
    private val fetchReviewsUseCase: FetchReviewsUseCase,
) : BaseViewModel<CompanyDetailsState, CompanyDetailsSideEffect>(CompanyDetailsState.getInitialState()) {

    internal fun setCompanyId(companyId: Long) = setState {
        state.value.copy(companyId = companyId)
    }

    internal fun fetchCompanyDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyDetailsUseCase(companyId = state.value.companyId).onSuccess {
                setState {
                    state.value.copy(
                        companyDetailsEntity = it,
                        buttonEnabled = it.recruitmentId != null,
                    )
                }
            }.onFailure {
                // TODO 스켈레톤 처리
                postSideEffect(CompanyDetailsSideEffect.FetchCompanyDetailsError)
            }
        }
    }

    internal fun fetchReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewsUseCase(companyId = state.value.companyId).onSuccess {
                val reviews = it.reviews
                setState {
                    state.value.copy(
                        reviews = reviews.take(MAX_REVIEW_COUNT),
                        showMoreReviews = reviews.size > MAX_REVIEW_COUNT,
                        showMoveToRecruitmentButton = true,
                    )
                }
            }
        }
    }

    internal fun onMoveToRecruitmentButtonClick() = setState {
        val recruitmentId = state.value.companyDetailsEntity.recruitmentId
        recruitmentId?.run {
            postSideEffect(CompanyDetailsSideEffect.MoveToRecruitmentDetails(recruitmentId = recruitmentId))
        }
        state.value.copy(buttonEnabled = false)
    }
}

@Immutable
internal data class CompanyDetailsState(
    val companyId: Long,
    val companyDetailsEntity: CompanyDetailsEntity,
    val reviews: List<FetchReviewsEntity.Review>,
    val showMoreReviews: Boolean,
    val showMoveToRecruitmentButton: Boolean,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = CompanyDetailsState(
            companyId = 0,
            companyDetailsEntity = CompanyDetailsEntity(
                businessNumber = "",
                companyName = "",
                companyProfileUrl = "",
                companyIntroduce = "",
                mainAddress = "",
                managerName = "",
                representativePhoneNo = "",
                email = "",
                representativeName = "",
                foundedAt = "",
                workerNumber = "",
                take = "",
                recruitmentId = 0,
                attachments = emptyList(),
                serviceName = "",
                businessArea = "",
                headquarter = false,
            ),
            reviews = emptyList(),
            showMoreReviews = false,
            showMoveToRecruitmentButton = false,
            buttonEnabled = true,
        )
    }
}

internal sealed interface CompanyDetailsSideEffect {
    data object FetchCompanyDetailsError : CompanyDetailsSideEffect
    data class MoveToRecruitmentDetails(val recruitmentId: Long) : CompanyDetailsSideEffect
}
