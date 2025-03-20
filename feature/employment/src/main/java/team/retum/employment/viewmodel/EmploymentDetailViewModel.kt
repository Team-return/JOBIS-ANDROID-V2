package team.retum.employment.viewmodel

import android.util.Log
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
import kotlin.experimental.ExperimentalTypeInference

@HiltViewModel
internal class EmploymentDetailViewModel @Inject constructor(
    private val fetchEmploymentStatusUseCase: FetchEmploymentStatusUseCase,
) : BaseViewModel<EmploymentDetailState, EmploymentDetailSideEffect>(EmploymentDetailState.getDefaultState()) {

    internal fun setClassId(classId: Int) = setState {
        state.value.copy(classId = classId)
    }

    internal fun upDateClassEmployment() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentStatusUseCase().onSuccess {
                setState {
                    state.value.copy(
                        totalStudent = it.classes[state.value.classId].totalStudents,
                        passStudent = it.classes[state.value.classId].passedStudents,
                    )
                }
            }
        }
    }

    internal fun fetchEmploymentStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentStatusUseCase().onSuccess {
                setState {
                    state.value.copy(
                        companyInfo = it.classes.flatMap { classInfo ->
                            classInfo.employmentRateList.map { employmentRate ->
                                CompanyItem(
                                    companyName = employmentRate.companyName,
                                    logoUrl = employmentRate.logoUrl,
                                )
                            }
                        }.toImmutableList(),
                    )
                }
            }
        }
    }
}

internal data class EmploymentDetailState(
    val classId: Int,
    val totalStudent: Int,
    val passStudent: Int,
    val companyInfo: ImmutableList<CompanyItem>,
) {
    companion object {
        fun getDefaultState() = EmploymentDetailState(
            classId = 0,
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
