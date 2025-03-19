package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.employment.model.CompanyItem
import team.retum.usecase.usecase.application.FetchEmploymentStatusUseCase
import javax.inject.Inject

@HiltViewModel
internal class EmploymentDetailViewModel @Inject constructor(
    private val fetchEmploymentStatusUseCase: FetchEmploymentStatusUseCase,
) : BaseViewModel<EmploymentDetailState, EmploymentDetailSideEffect>(EmploymentDetailState.getDefaultState()) {
    init {
        fetchEmploymentStatus()
    }

    private fun fetchEmploymentStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentStatusUseCase().onSuccess {
                setState {
                    state.value.copy(
                        totalStudent = it.classes.firstOrNull { it.classId == 1 }?.totalStudents,
                        companyInfo = it.classes.flatMap { classInfo ->
                            classInfo.employmentRateList.map { employmentRate ->
                                CompanyItem(
                                    companyName = employmentRate.companyName,
                                    logoUrl = employmentRate.logoUrl,
                                )
                            }
                        }.apply {
                            if (isNullOrEmpty()) listOf(
                                state.value.totalStudent?.let {
                                    repeat(it){
                                        CompanyItem(
                                            companyName = "",
                                            logoUrl = "",
                                        )
                                    }
                                }
                            )
                        }.toImmutableList(),
                    )
                }
            }
        }
    }

    private fun upDateClassEmployment() {

    }
}

internal data class EmploymentDetailState(
    val totalStudent: Int?,
    val passStudent: Long,
    val companyInfo: ImmutableList<CompanyItem>,
) {
    companion object {
        fun getDefaultState() = EmploymentDetailState(
            totalStudent = 0,
            passStudent = 0,
            companyInfo = persistentListOf(),
        )
    }
}

internal sealed interface EmploymentDetailSideEffect {
    data object BadRequest : EmploymentDetailSideEffect
    data object Success : EmploymentDetailSideEffect
}
