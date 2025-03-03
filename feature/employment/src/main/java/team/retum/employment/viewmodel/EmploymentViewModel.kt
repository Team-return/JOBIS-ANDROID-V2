package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import team.retum.usecase.usecase.application.FetchEmploymentStatusUseCase
import java.text.DecimalFormat
import javax.inject.Inject

class EmploymentViewModel @Inject constructor(
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
) : BaseViewModel<EmploymentState, EmploymentSideEffect>(EmploymentState.getDefaultState()) {
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
}

data class EmploymentState(
    val rate: String,
    val totalStudentCount: Long,
    val passCount: Long,
) {
    companion object {
        fun getDefaultState() = EmploymentState(
            rate = "",
            totalStudentCount = 0,
            passCount = 0,
        )
    }
}

sealed interface EmploymentSideEffect { // 익셉션의 집합
    data object BadRequest : EmploymentSideEffect
    data object Success : EmploymentSideEffect
}
