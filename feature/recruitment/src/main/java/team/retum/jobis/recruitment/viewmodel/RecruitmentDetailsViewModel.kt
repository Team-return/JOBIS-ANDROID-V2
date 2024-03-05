package team.retum.jobis.recruitment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.RecruitmentDetailsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentDetailsUseCase
import javax.inject.Inject

@HiltViewModel
internal class RecruitmentDetailsViewModel @Inject constructor(
    private val fetchRecruitmentDetailsUseCase: FetchRecruitmentDetailsUseCase,
    private val bookmarkRecruitmentUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<RecruitmentDetailsState, RecruitmentDetailsSideEffect>(RecruitmentDetailsState.getDefaultState()) {

    internal fun fetchRecruitmentDetails(recruitmentId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRecruitmentDetailsUseCase(recruitmentId = recruitmentId!!)
                .onSuccess { detail ->
                    with(detail) {
                        setState {
                            state.value.copy(
                                recruitmentDetailsEntity = copy(
                                    companyProfileUrl = ResourceKeys.IMAGE_URL + companyProfileUrl,
                                ),
                            )
                        }
                    }
                }
                .onFailure {
                    when (it) {
                        is NullPointerException -> {
                            postSideEffect(RecruitmentDetailsSideEffect.BadRequest)
                        }
                    }
                }
        }
    }

    internal fun bookmarkRecruitmentDetail(recruitmentId: Long) {
        val entity = state.value.recruitmentDetailsEntity
        val bookmarked = state.value.recruitmentDetailsEntity.bookmarked
        setState { state.value.copy(recruitmentDetailsEntity = entity.copy(bookmarked = !bookmarked)) }
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRecruitmentUseCase(recruitmentId)
        }
    }
}

internal data class RecruitmentDetailsState(
    val companyId: Long,
    val recruitmentDetailsEntity: RecruitmentDetailsEntity,
) {
    companion object {
        fun getDefaultState() = RecruitmentDetailsState(
            companyId = 0,
            recruitmentDetailsEntity = RecruitmentDetailsEntity(
                companyProfileUrl = "",
                companyName = "",
                areas = emptyList(),
                requiredGrade = null,
                startTime = "",
                endTime = "",
                requiredLicenses = null,
                hiringProgress = emptyList(),
                trainPay = 0,
                pay = null,
                benefits = null,
                military = false,
                submitDocument = "",
                startDate = "",
                endDate = "",
                etc = null,
                isApplicable = false,
                bookmarked = false,
            ),
        )
    }
}

sealed interface RecruitmentDetailsSideEffect {
    data object BadRequest : RecruitmentDetailsSideEffect
}
