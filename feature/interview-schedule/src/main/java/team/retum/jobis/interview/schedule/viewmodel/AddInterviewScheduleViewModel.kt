package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.HiringProgress
import team.retum.jobis.interview.schedule.util.CalendarEventData
import team.retum.usecase.usecase.company.FetchCompaniesUseCase
import team.retum.usecase.usecase.company.FetchCompanyDetailsUseCase
import team.retum.usecase.usecase.interview.SetInterviewUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class AddInterviewScheduleViewModel @Inject constructor(
    private val setInterviewUseCase: SetInterviewUseCase,
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchCompaniesUseCase: FetchCompaniesUseCase,
    private val fetchCompanyDetailsUseCase: FetchCompanyDetailsUseCase,
) : BaseViewModel<WriteInterviewScheduleState, AddInterviewScheduleSideEffect>(
    WriteInterviewScheduleState.getInitialState(),
) {

    private var searchJob: Job? = null

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private const val DEBOUNCE_DELAY = 500L
    }

    init {
        fetchStudentId()
    }

    private fun fetchStudentId() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                setState { state.value.copy(studentId = it.studentId) }
            }
        }
    }

    internal fun setCompany(company: String) {
        setState {
            state.value.copy(company = company).updateButtonEnabled()
        }
        searchJob?.cancel()
        if (company.isBlank()) {
            setState {
                state.value.copy(
                    companySuggestions = emptyList(),
                    showCompanySuggestions = false,
                )
            }
            return
        }
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DEBOUNCE_DELAY)
            fetchCompaniesUseCase(page = 1, name = company).onSuccess { result ->
                val suggestions = result.companies.map {
                    CompanySuggestion(id = it.id, name = it.name)
                }
                setState {
                    state.value.copy(
                        companySuggestions = suggestions,
                        showCompanySuggestions = suggestions.isNotEmpty(),
                    )
                }
            }
        }
    }

    internal fun onCompanySelected(id: Long, name: String) {
        searchJob?.cancel()
        setState {
            state.value.copy(
                company = name,
                companySuggestions = emptyList(),
                showCompanySuggestions = false,
            ).updateButtonEnabled()
        }
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyDetailsUseCase(companyId = id).onSuccess { details ->
                details.mainAddress?.let { address ->
                    setState {
                        state.value.copy(location = address).updateButtonEnabled()
                    }
                }
            }
        }
    }

    internal fun dismissCompanySuggestions() {
        setState {
            state.value.copy(showCompanySuggestions = false)
        }
    }

    internal fun setLocation(location: String) {
        setState {
            state.value.copy(location = location).updateButtonEnabled()
        }
    }

    internal fun onHiringProgressSelected(hiringProgress: HiringProgress) {
        setState {
            state.value.copy(
                hiringProgress = hiringProgress,
                showHiringProgressDropdown = false,
            ).updateButtonEnabled()
        }
    }

    internal fun onHiringProgressDropdownClick() {
        setState {
            state.value.copy(showHiringProgressDropdown = !state.value.showHiringProgressDropdown)
        }
    }

    internal fun setStartDate(date: LocalDate) {
        setState {
            val newEndDate = if (date.isAfter(state.value.endDate)) date else state.value.endDate
            state.value.copy(
                startDate = date,
                endDate = newEndDate,
                showDatePicker = null,
            )
        }
    }

    internal fun setEndDate(date: LocalDate) {
        setState {
            state.value.copy(
                endDate = date,
                showDatePicker = null,
            )
        }
    }

    internal fun setIsDateRange(isDateRange: Boolean) {
        setState {
            state.value.copy(isDateRange = isDateRange)
        }
    }

    internal fun setTime(time: String) {
        setState {
            state.value.copy(time = time).updateButtonEnabled()
        }
    }

    internal fun onStartDateClick() {
        setState {
            state.value.copy(showDatePicker = DatePickerTarget.START)
        }
    }

    internal fun onEndDateClick() {
        setState {
            state.value.copy(showDatePicker = DatePickerTarget.END)
        }
    }

    internal fun onDismissDatePicker() {
        setState {
            state.value.copy(showDatePicker = null)
        }
    }

    internal fun onSaveClick() {
        saveInterview()
    }

    internal fun showCalendarDialog(eventData: CalendarEventData) {
        setState {
            state.value.copy(
                showCalendarDialog = true,
                pendingCalendarEvent = eventData,
            )
        }
    }

    internal fun dismissCalendarDialog() {
        setState {
            state.value.copy(
                showCalendarDialog = false,
                pendingCalendarEvent = null,
            )
        }
    }

    private fun saveInterview() {
        val currentState = state.value
        if (currentState.isLoading) return

        setState { state.value.copy(isLoading = true) }

        val formattedTime = runCatching {
            val timeParts = currentState.time.replace(".", ":").split(":")
            val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
            val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
            String.format("%02d:%02d", hour, minute)
        }.getOrDefault(currentState.time)

        viewModelScope.launch(Dispatchers.IO) {
            setInterviewUseCase(
                interviewType = currentState.hiringProgress,
                startDate = currentState.startDate.format(DATE_FORMATTER),
                endDate = if (currentState.isDateRange) {
                    currentState.endDate.format(DATE_FORMATTER)
                } else {
                    currentState.startDate.format(DATE_FORMATTER)
                },
                interviewTime = formattedTime,
                companyName = currentState.company,
                location = currentState.location,
                studentId = currentState.studentId,
            ).onSuccess {
                setState { state.value.copy(isLoading = false) }
                val eventData = CalendarEventData(
                    title = "${currentState.company} 면접",
                    description = "면접 유형: ${currentState.hiringProgress.value}",
                    location = currentState.location,
                    startDate = currentState.startDate.format(DATE_FORMATTER),
                    endDate = if (currentState.isDateRange) {
                        currentState.endDate.format(DATE_FORMATTER)
                    } else {
                        currentState.startDate.format(DATE_FORMATTER)
                    },
                    interviewTime = formattedTime,
                )
                postSideEffect(AddInterviewScheduleSideEffect.AddSuccessRequestCalendar(eventData))
            }.onFailure {
                setState { state.value.copy(isLoading = false) }
                postSideEffect(AddInterviewScheduleSideEffect.AddFailed)
            }
        }
    }
}

internal sealed interface AddInterviewScheduleSideEffect {
    data class AddSuccessRequestCalendar(val eventData: CalendarEventData) : AddInterviewScheduleSideEffect
    data object AddFailed : AddInterviewScheduleSideEffect
}

@Immutable
internal data class WriteInterviewScheduleState(
    val isEditMode: Boolean,
    val studentId: Long,
    val company: String,
    val location: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isDateRange: Boolean,
    val time: String,
    val showDatePicker: DatePickerTarget?,
    val showConfirmDialog: Boolean,
    val isLoading: Boolean,
    val buttonEnabled: Boolean,
    val hiringProgress: HiringProgress,
    val showHiringProgressDropdown: Boolean,
    val showCalendarDialog: Boolean,
    val pendingCalendarEvent: CalendarEventData?,
    val companySuggestions: List<CompanySuggestion>,
    val showCompanySuggestions: Boolean,
) {
    companion object {
        fun getInitialState() = WriteInterviewScheduleState(
            isEditMode = false,
            studentId = 0L,
            company = "",
            location = "",
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            isDateRange = false,
            time = "",
            showDatePicker = null,
            showConfirmDialog = false,
            isLoading = false,
            buttonEnabled = false,
            hiringProgress = HiringProgress.DOCUMENT,
            showHiringProgressDropdown = false,
            showCalendarDialog = false,
            pendingCalendarEvent = null,
            companySuggestions = emptyList(),
            showCompanySuggestions = false,
        )
    }

    fun updateButtonEnabled(): WriteInterviewScheduleState {
        return copy(
            buttonEnabled = company.isNotBlank() && location.isNotBlank() && time.isNotBlank() && hiringProgress != HiringProgress.DOCUMENT,
        )
    }
}

@Immutable
internal data class CompanySuggestion(
    val id: Long,
    val name: String,
)

internal enum class DatePickerTarget {
    START,
    END,
}
