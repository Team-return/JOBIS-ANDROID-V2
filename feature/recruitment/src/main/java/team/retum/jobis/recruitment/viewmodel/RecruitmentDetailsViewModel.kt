package team.retum.jobis.recruitment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.RecruitmentDetailsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentDetailsUseCase
import javax.inject.Inject

@HiltViewModel
internal class RecruitmentDetailsViewModel @Inject constructor(
    private val fetchRecruitmentDetailsUseCase: FetchRecruitmentDetailsUseCase,
    private val bookmarkRecruitmentUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<RecruitmentDetailsState, RecruitmentDetailsSideEffect>(RecruitmentDetailsState.getDefaultState()) {

    internal fun fetchRecruitmentDetails(recruitmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRecruitmentDetailsUseCase(recruitmentId = recruitmentId).onSuccess { detail ->
                setState {
                    state.value.copy(recruitmentDetailsEntity = detail)
                }
            }.onFailure {
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

    internal fun onMoveToCompanyDetailsClick() = setState {
        postSideEffect(RecruitmentDetailsSideEffect.MoveToCompanyDetails(companyId = state.value.recruitmentDetailsEntity.companyId))
        state.value.copy(buttonEnabled = false)
    }
}

internal data class RecruitmentDetailsState(
    val recruitmentDetailsEntity: RecruitmentDetailsEntity,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getDefaultState() = RecruitmentDetailsState(
            recruitmentDetailsEntity = RecruitmentDetailsEntity(
                companyId = 0L,
                companyProfileUrl = "",
                companyName = "",
                areas = emptyList(),
                requiredGrade = null,
                workingHours = "",
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
            buttonEnabled = false,
        )
    }
}

sealed interface RecruitmentDetailsSideEffect {
    data object BadRequest : RecruitmentDetailsSideEffect
    data class MoveToCompanyDetails(val companyId: Long) : RecruitmentDetailsSideEffect
}
