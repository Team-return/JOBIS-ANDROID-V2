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

    private fun updateFilterYear() {
        val startYear = 2024
        val endYear = LocalDate.now().year

        setState { state.value.copy(yearList = (startYear..endYear).map { it.toString() }.reversed().toPersistentList()) }
    }

    internal fun setYear(selectedYear: String) {
        setState { state.value.copy(selectedYear = selectedYear) }
    }
}

internal data class EmploymentState(
    val rate: Float,
    val totalStudentCount: String,
    val passCount: String,
    val selectedYear: String,
    val yearList: ImmutableList<String>,
) {
    companion object {
        fun getDefaultState() = EmploymentState(
            rate = 0F,
            totalStudentCount = "",
            passCount = "",
            selectedYear = LocalDate.now().year.toString(),
            yearList = persistentListOf(),
        )
    }
}

internal sealed interface EmploymentSideEffect {
    data object FetchEmploymentCountError : EmploymentSideEffect
}
