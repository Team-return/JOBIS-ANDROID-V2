package team.retum.home.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Department
import team.retum.common.exception.NotFoundException
import team.retum.common.model.ApplicationData
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.application.AppliedCompaniesEntity
import team.retum.usecase.entity.banner.BannersEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import team.retum.usecase.intern.FetchWinterInternUseCase
import team.retum.usecase.usecase.application.FetchAppliedCompaniesUseCase
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import team.retum.usecase.usecase.application.FetchRejectionReasonUseCase
import team.retum.usecase.usecase.banner.FetchBannersUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import java.text.DecimalFormat
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

private const val SCHOOL_ESTABLISHMENT = 2015

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchAppliedCompaniesUseCase: FetchAppliedCompaniesUseCase,
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
    private val fetchRejectionReasonUseCase: FetchRejectionReasonUseCase,
    private val fetchBannersUseCase: FetchBannersUseCase,
    private val fetchWinterInternUseCase: FetchWinterInternUseCase,
) : BaseViewModel<HomeState, HomeSideEffect>(HomeState.getDefaultState()) {

    internal fun calculateTerm() = setState {
        val term = LocalDate.now().year - SCHOOL_ESTABLISHMENT - 1
        state.value.copy(term = term)
    }

    internal fun fetchBanners() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchBannersUseCase().onSuccess {
                setState { state.value.copy(banners = it.banners) }
            }
        }
    }

    internal fun fetchWinterIntern() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchWinterInternUseCase().onSuccess {
                setState { state.value.copy(isWinterIntern = it) }
            }
        }
    }

    internal fun fetchStudentInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                val profileImageUrl = ResourceKeys.IMAGE_URL + it.profileImageUrl
                setState { state.value.copy(studentInformation = it.copy(profileImageUrl = profileImageUrl)) }
            }
        }
    }

    internal fun fetchAppliedCompanies() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAppliedCompaniesUseCase().onSuccess { appliedCompaniesEntity ->
                setState { state.value.copy(appliedCompanies = appliedCompaniesEntity.applications) }
            }
        }
    }

    internal fun fetchEmploymentCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentCountUseCase().onSuccess {
                setState {
                    val rate = if (it.passCount == 0L || it.totalStudentCount == 0L) {
                        0f
                    } else {
                        it.passCount.toFloat() / it.totalStudentCount.toFloat() * 100f
                    }
                    state.value.copy(
                        rate = DecimalFormat("##.#").format(rate),
                        totalStudentCount = it.totalStudentCount,
                        passCount = it.passCount,
                    )
                }
            }
        }
    }

    internal fun onRejectionReasonClick(applicationData: ApplicationData) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRejectionReasonUseCase(applicationId = applicationData.applicationId).onSuccess {
                postSideEffect(
                    HomeSideEffect.ShowRejectionModal(
                        applicationData = applicationData.copy(rejectionReason = it.rejectionReason),
                    ),
                )
            }.onFailure {
                when (it) {
                    is NotFoundException -> {
                        postSideEffect(HomeSideEffect.NotFoundApplication)
                    }
                }
            }
        }
    }

    internal fun fetchScroll(
        applicationId: Long?,
        position: Float,
        enabled: Boolean,
    ) {
        if (applicationId != null && enabled) {
            postSideEffect(HomeSideEffect.ScrollToApplication(position.roundToInt()))
        }
    }
}

@Immutable
internal data class HomeState(
    val studentInformation: StudentInformationEntity,
    val rate: String,
    val totalStudentCount: Long,
    val passCount: Long,
    val term: Int,
    val banners: List<BannersEntity.BannerEntity>,
    val appliedCompanies: List<AppliedCompaniesEntity.ApplicationEntity>,
    val isWinterIntern: Boolean,
) {
    companion object {
        fun getDefaultState() = HomeState(
            studentInformation = StudentInformationEntity(
                studentName = "",
                studentGcn = "",
                department = Department.SOFTWARE_DEVELOP,
                profileImageUrl = "",
            ),
            rate = "",
            totalStudentCount = 0,
            passCount = 0,
            term = 0,
            banners = emptyList(),
            appliedCompanies = emptyList(),
            isWinterIntern = false,
        )
    }
}

internal sealed interface HomeSideEffect {
    data class ShowRejectionModal(val applicationData: ApplicationData) : HomeSideEffect

    data object NotFoundApplication : HomeSideEffect
    data class ScrollToApplication(val sectionOneCoordinates: Int) : HomeSideEffect
}
