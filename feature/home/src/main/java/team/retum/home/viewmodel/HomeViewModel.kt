package team.retum.home.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Department
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.application.AppliedCompaniesEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import team.retum.usecase.usecase.application.FetchAppliedCompaniesUseCase
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchAppliedCompaniesUseCase: FetchAppliedCompaniesUseCase,
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
) : BaseViewModel<HomeState, HomeSideEffect>(HomeState.getDefaultState()) {

    internal var appliedCompanies: SnapshotStateList<AppliedCompaniesEntity.ApplicationEntity> =
        mutableStateListOf()
        private set

    init {
        fetchStudentInformation()
        fetchAppliedCompanies()
        fetchEmploymentCount()
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

}
