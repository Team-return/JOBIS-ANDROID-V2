package team.retum.employment.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.usecase.application.FetchEmploymentCountUseCase
import java.text.DecimalFormat
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class EmploymentViewModel @Inject constructor(
    private val fetchEmploymentCountUseCase: FetchEmploymentCountUseCase,
) : BaseViewModel<EmploymentState, EmploymentSideEffect>(EmploymentState.getDefaultState()) {

    init {
        updateFilterYear()
    }

    internal fun fetchEmploymentCount(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchEmploymentCountUseCase(year = year)
                .onSuccess {
                    setState {
                        val rate = if (it.passCount == 0L || it.totalStudentCount == 0L) {
                            0f
                        } else {
                            it.passCount.toFloat() / it.totalStudentCount.toFloat() * 100f
                        }
                        state.value.copy(
                            rate = DecimalFormat("##.#").format(rate).toFloat(),
                            totalStudentCount = it.totalStudentCount,
                            passCount = it.passCount,
                        )
                    }
                }
                .onFailure {
                    postSideEffect(EmploymentSideEffect.FetchEmploymentCountError)
                }
        }
    }

    private fun updateFilterYear() {
        val startYear = 2024
        val endYear = LocalDate.now().year

        setState { state.value.copy(yearList = (startYear..endYear).map { it }.reversed().toPersistentList()) }
    }

    internal fun setSelectedYear(year: Int) {
        setState {
            state.value.copy(
                selectedYear = if (state.value.selectedYear == year) 0 else year,
            )
        }
    }
}

internal data class EmploymentState(
    val rate: Float,
    val totalStudentCount: Long,
    val passCount: Long,
    val selectedYear: Int,
    val yearList: ImmutableList<Int>,
) {
    companion object {
        fun getDefaultState() = EmploymentState(
            rate = 0F,
            totalStudentCount = 0,
            passCount = 0,
            selectedYear = LocalDate.now().year,
            yearList = persistentListOf(),
        )
    }
}

internal sealed interface EmploymentSideEffect {
    data object FetchEmploymentCountError : EmploymentSideEffect
}
