package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
internal class EmploymentViewModel @Inject constructor(
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
) : BaseViewModel<EmploymentState, EmploymentSideEffect>(EmploymentState.getDefaultState()) {
    internal fun fetchEmploymentCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentCountUseCase()
                .onSuccess {
                    setState {
                        val rate = if (it.passCount == 0L || it.totalStudentCount == 0L) {
                            0f
                        } else {
                            it.passCount.toFloat() / it.totalStudentCount.toFloat() * 100f
                        }
                        state.value.copy(
                            rate = DecimalFormat("##.#").format(rate).toFloat(),
                            totalStudentCount = it.totalStudentCount.toString(),
                            passCount = it.passCount.toString(),
                        )
                    }
                }
                .onFailure {
                    postSideEffect(EmploymentSideEffect.FetchEmploymentCountError)
                }
        }
    }
}

internal data class EmploymentState(
    val rate: Float,
    val totalStudentCount: String,
    val passCount: String,
) {
    companion object {
        fun getDefaultState() = EmploymentState(
            rate = 0F,
            totalStudentCount = "",
            passCount = "",
        )
    }
}

internal sealed interface EmploymentSideEffect {
    data object FetchEmploymentCountError : EmploymentSideEffect
}
