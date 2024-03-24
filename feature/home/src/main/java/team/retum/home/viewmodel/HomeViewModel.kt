package team.retum.home.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Department
import team.retum.common.exception.NotFoundException
import team.retum.common.model.ApplicationData
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.application.AppliedCompaniesEntity
import team.retum.usecase.entity.banner.BannersEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import team.retum.usecase.usecase.application.FetchAppliedCompaniesUseCase
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import team.retum.usecase.usecase.application.FetchRejectionReasonUseCase
import team.retum.usecase.usecase.banner.FetchBannersUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import javax.inject.Inject
import kotlin.math.roundToInt

internal const val INITIAL_BANNER_SIZE = 1

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchAppliedCompaniesUseCase: FetchAppliedCompaniesUseCase,
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
    private val fetchBannersUseCase: FetchBannersUseCase,
    private val fetchRejectionReasonUseCase: FetchRejectionReasonUseCase,
) : BaseViewModel<HomeState, HomeSideEffect>(HomeState.getDefaultState()) {

    internal var appliedCompanies: MutableList<AppliedCompaniesEntity.ApplicationEntity> =
        mutableListOf()
        private set

    internal var banners: SnapshotStateList<BannersEntity.BannerEntity> = mutableStateListOf()
        private set

    init {
        fetchStudentInformation()
        fetchAppliedCompanies()
        fetchEmploymentCount()
        fetchBanners()
    }

    private fun fetchStudentInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                val profileImageUrl = ResourceKeys.IMAGE_URL + it.profileImageUrl
                setState { state.value.copy(studentInformation = it.copy(profileImageUrl = profileImageUrl)) }
            }
        }
    }

    private fun fetchAppliedCompanies() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAppliedCompaniesUseCase().onSuccess {
                appliedCompanies.addAll(
                    it.applications.map {
                        it.copy(companyLogoUrl = ResourceKeys.IMAGE_URL + it.companyLogoUrl)
                    },
                )
            }
        }
    }

    private fun fetchEmploymentCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentCountUseCase().onSuccess {
                setState {
                    val rate = if (it.passCount == 0L || it.totalStudentCount == 0L) {
                        0f
                    } else {
                        it.passCount / it.totalStudentCount * 100f
                    }
                    state.value.copy(
                        rate = rate,
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

    private fun fetchBanners() = runBlocking {
        fetchBannersUseCase().onSuccess {
            banners.addAll(it.banners)
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

internal data class HomeState(
    val studentInformation: StudentInformationEntity,
    val rate: Float,
    val totalStudentCount: Long,
    val passCount: Long,
) {
    companion object {
        fun getDefaultState() = HomeState(
            studentInformation = StudentInformationEntity(
                studentName = "",
                studentGcn = "",
                department = Department.SOFTWARE_DEVELOP,
                profileImageUrl = "",
            ),
            rate = 0f,
            totalStudentCount = 0,
            passCount = 0,
        )
    }
}

internal sealed interface HomeSideEffect {
    data class ShowRejectionModal(val applicationData: ApplicationData) : HomeSideEffect

    data object NotFoundApplication : HomeSideEffect
    data class ScrollToApplication(val sectionOneCoordinates: Int) : HomeSideEffect
}
