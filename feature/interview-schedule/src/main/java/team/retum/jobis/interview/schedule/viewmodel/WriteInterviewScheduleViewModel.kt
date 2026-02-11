package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.HiringProgress
import team.retum.jobis.interview.schedule.navigation.ARG_INTERVIEW_ID
import team.retum.jobis.interview.schedule.util.CalendarEventData
import team.retum.usecase.usecase.interests.FetchInterestsUseCase
import team.retum.usecase.usecase.interview.FetchInterviewUseCase
import team.retum.usecase.usecase.interview.SetInterviewUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class WriteInterviewScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val setInterviewUseCase: SetInterviewUseCase,
    private val fetchInterviewUseCase: FetchInterviewUseCase,
    private val fetchInterestsUseCase: FetchInterestsUseCase,
) : BaseViewModel<WriteInterviewScheduleState, WriteInterviewScheduleSideEffect>(
    WriteInterviewScheduleState.getInitialState()
) {

    private val interviewId: Long = savedStateHandle.get<Long>(ARG_INTERVIEW_ID) ?: 0L
    private val isEditMode: Boolean get() = interviewId != 0L

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    init {
        setState { state.value.copy(isEditMode = isEditMode) }
        fetchStudentId()
        if (isEditMode) {
            fetchInterviewDetails()
        }
    }

    private fun fetchStudentId() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterestsUseCase().onSuccess { interestsEntity ->
                if (interestsEntity.interests.isNotEmpty()) {
                    val id = interestsEntity.interests.first().studentId.toLong()
                    setState { state.value.copy(studentId = id) }
                }
            }
        }
    }

    private fun fetchInterviewDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterviewUseCase().onSuccess { interviews ->
                val interview = interviews.interviews.find { it.id == interviewId }
                interview?.let {
                    val startDate = runCatching {
                        LocalDate.parse(it.startDate)
                    }.getOrDefault(LocalDate.now())

                    val endDate = runCatching {
                        it.endDate?.let { date -> LocalDate.parse(date) }
                    }.getOrNull()

                    val isDateRange = endDate != null && endDate != startDate

                    setState {
                        state.value.copy(
                            company = it.companyName,
                            hiringProgress = it.interviewType,
                            location = it.location,
                            startDate = startDate,
                            endDate = endDate ?: startDate,
                            isDateRange = isDateRange,
                            time = it.interviewTime.replace(":", "."),
                        ).updateButtonEnabled()
                    }
                }
            }
        }
    }

    internal fun setCompany(company: String) {
        setState {
            state.value.copy(company = company).updateButtonEnabled()
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

    internal fun onConfirmEditClick() {
        setState {
            state.value.copy(showConfirmDialog = true)
        }
    }

    internal fun onDismissConfirmDialog() {
        setState {
            state.value.copy(showConfirmDialog = false)
        }
    }

    internal fun onSaveClick() {
        if (isEditMode) {
            onConfirmEditClick()
        } else {
            saveInterview()
        }
    }

    internal fun onConfirmEdit() {
        setState { state.value.copy(showConfirmDialog = false) }
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
                if (isEditMode) {
                    postSideEffect(WriteInterviewScheduleSideEffect.EditSuccess)
                } else {
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
                    postSideEffect(WriteInterviewScheduleSideEffect.AddSuccessRequestCalendar(eventData))
                }
            }.onFailure {
                setState { state.value.copy(isLoading = false) }
                postSideEffect(
                    if (isEditMode) {
                        WriteInterviewScheduleSideEffect.EditFailed
                    } else {
                        WriteInterviewScheduleSideEffect.AddFailed
                    }
                )
            }
        }
    }
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
        )
    }

    fun updateButtonEnabled(): WriteInterviewScheduleState {
        return copy(
            buttonEnabled = company.isNotBlank() &&
                    location.isNotBlank() &&
                    time.isNotBlank() &&
                    hiringProgress != HiringProgress.DOCUMENT
        )
    }
}

internal enum class DatePickerTarget {
    START,
    END,
}

internal sealed interface WriteInterviewScheduleSideEffect {
    data class AddSuccessRequestCalendar(val eventData: CalendarEventData) : WriteInterviewScheduleSideEffect
    data object AddFailed : WriteInterviewScheduleSideEffect
    data object EditSuccess : WriteInterviewScheduleSideEffect
    data object EditFailed : WriteInterviewScheduleSideEffect
}
