package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.application.EmploymentStatusEntity
import team.retum.usecase.usecase.application.FetchEmploymentStatusUseCase
import javax.inject.Inject

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
                        classInfoList = it.classes[state.value.classId].employmentRateList.toMutableList(),
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
    val classInfoList: MutableList<EmploymentStatusEntity.ClassEmploymentStatusEntity.GetEmploymentRateList>,
) {
    companion object {
        fun getDefaultState() = EmploymentDetailState(
            classId = 0,
            totalStudent = 0,
            passStudent = 0,
            classInfoList = mutableListOf(),
        )
    }
}

internal sealed interface EmploymentDetailSideEffect {
    data object BadRequest : EmploymentDetailSideEffect
    data object Success : EmploymentDetailSideEffect
}
