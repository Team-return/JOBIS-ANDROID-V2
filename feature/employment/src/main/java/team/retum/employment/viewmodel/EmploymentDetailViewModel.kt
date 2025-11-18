package team.retum.employment.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.application.EmploymentStatusEntity
import team.retum.usecase.usecase.application.FetchEmploymentStatusUseCase
import java.time.LocalDate
import javax.inject.Inject

private const val MAX_STUDENT = 16

@HiltViewModel
internal class EmploymentDetailViewModel @Inject constructor(
    private val fetchEmploymentStatusUseCase: FetchEmploymentStatusUseCase,
) : BaseViewModel<EmploymentDetailState, EmploymentDetailSideEffect>(EmploymentDetailState.getDefaultState()) {

    init {
        setClassId(classId = state.value.classId)
    }

    internal fun setClassId(classId: Int) = setState {
        state.value.copy(classId = classId)
    }

    internal fun fetchEmploymentStatus(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentStatusUseCase(year = year).onSuccess {
                val classData = it.classes[state.value.classId]
                val displayList = buildDisplayList(
                    employmentRateList = classData.employmentRateList,
                    passedStudents = classData.passedStudents
                )

                setState {
                    state.value.copy(
                        classInfoList = displayList.toImmutableList(),
                        totalStudent = classData.totalStudents,
                        passStudent = classData.passedStudents,
                    )
                }
            }
        }
    }

    private fun buildDisplayList(
        employmentRateList: List<EmploymentStatusEntity.ClassEmploymentStatusEntity.FetchEmploymentRateList>,
        passedStudents: Int,
    ): List<EmploymentStatusEntity.ClassEmploymentStatusEntity.FetchEmploymentRateList> {
        val emptyCardCount = MAX_STUDENT - passedStudents
        val emptyCards = List(emptyCardCount) {
            EmploymentStatusEntity.ClassEmploymentStatusEntity.FetchEmploymentRateList(
                id = 0,
                companyName = "",
                logoUrl = "",
            )
        }
        return employmentRateList + emptyCards
    }
}

@Immutable
internal data class EmploymentDetailState(
    val classId: Int,
    val totalStudent: Int,
    val passStudent: Int,
    val classInfoList: ImmutableList<EmploymentStatusEntity.ClassEmploymentStatusEntity.FetchEmploymentRateList>,
    val employmentYear: Int,
) {
    companion object {
        fun getDefaultState() = EmploymentDetailState(
            classId = 0,
            totalStudent = 0,
            passStudent = 0,
            classInfoList = kotlinx.collections.immutable.persistentListOf(),
            employmentYear = LocalDate.now().year,
        )
    }
}

internal sealed interface EmploymentDetailSideEffect {
    data object BadRequest : EmploymentDetailSideEffect
    data object Success : EmploymentDetailSideEffect
}
