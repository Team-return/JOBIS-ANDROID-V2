package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.employment.model.CompanyItem
import team.retum.usecase.usecase.application.FetchEmploymentStatusUseCase
import javax.inject.Inject

class EmploymentDetailViewModel @Inject constructor(
    private val fetchEmploymentStatusUseCase: FetchEmploymentStatusUseCase
) : BaseViewModel<EmploymentDetailState, EmploymentDetailSideEffect>(EmploymentDetailState.getDefaultState()) {
    internal fun fetchEmploymentStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentStatusUseCase().onSuccess {
                setState {
                    state.value.copy(
                        companyInfo = it.classes.flatMap { classInfo ->
                            classInfo.employmentRateList.map { employmentRate ->
                                CompanyItem(
                                    companyName = employmentRate.companyName,
                                    logoUrl = employmentRate.logoUrl
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

data class EmploymentDetailState(
    val classId: Long,
    val totalStudent: Long,
    val passStudent: Long,
    val companyInfo: List<CompanyItem>
) {
    companion object {
        fun getDefaultState() = EmploymentDetailState(
            classId = 0,
            totalStudent = 0,
            passStudent = 0,
            companyInfo = emptyList(),
        )
    }

}

sealed interface EmploymentDetailSideEffect {
    data object BadRequest : EmploymentDetailSideEffect
    data object Success : EmploymentDetailSideEffect
}
