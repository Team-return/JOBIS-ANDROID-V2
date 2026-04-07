package team.retum.jobis.interview.schedule.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.HiringProgress
import team.retum.jobis.interview.schedule.navigation.INTERVIEW_ID
import team.retum.usecase.usecase.interview.FetchInterviewUseCase
import team.retum.usecase.usecase.interview.SetInterviewUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class EditInterviewScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val setInterviewUseCase: SetInterviewUseCase,
    private val fetchInterviewUseCase: FetchInterviewUseCase,
) : BaseViewModel<InterviewScheduleFormState, EditInterviewScheduleSideEffect>(
    InterviewScheduleFormState.getInitialState().copy(isEditMode = true),
) {

    private val interviewId: Long? = savedStateHandle.get<Long>(INTERVIEW_ID)?.takeIf { it != 0L }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    init {
        if (interviewId == null) {
            postSideEffect(EditInterviewScheduleSideEffect.NavigateBack)
        } else {
            fetchInterviewDetails()
        }
    }

    private fun fetchInterviewDetails() {
        val id = interviewId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterviewUseCase().onSuccess { interviews ->
                val interview = interviews.interviews.find { it.id == id }
                if (interview == null) {
                    postSideEffect(EditInterviewScheduleSideEffect.NavigateBack)
                    return@launch
                }

                val startDate = runCatching {
                    LocalDate.parse(interview.startDate)
                }.getOrDefault(LocalDate.now())

                val endDate = runCatching {
                    interview.endDate?.let { date -> LocalDate.parse(date) }
                }.getOrNull()

                val isDateRange = endDate != null && endDate != startDate

                setState {
                    state.value.copy(
                        company = interview.companyName,
                        hiringProgress = interview.interviewType,
                        location = interview.location,
                        startDate = startDate,
                        endDate = endDate ?: startDate,
                        isDateRange = isDateRange,
                        time = interview.interviewTime.replace(":", "."),
                    ).updateButtonEnabled()
                }
            }.onFailure {
                postSideEffect(EditInterviewScheduleSideEffect.NavigateBack)
            }
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
        setState {
            state.value.copy(showConfirmDialog = true)
        }
    }

    internal fun onDismissConfirmDialog() {
        setState {
            state.value.copy(showConfirmDialog = false)
        }
    }

    internal fun onConfirmEdit() {
        setState { state.value.copy(showConfirmDialog = false) }
        saveInterview()
    }

    private fun saveInterview() {
        val currentState = state.value
        if (currentState.isLoading) return

        setState { state.value.copy(isLoading = true) }

        val formattedTime = runCatching {
            val timeParts = currentState.time.replace(".", ":").split(":")
            val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
            val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
            String.format(Locale.KOREA, "%02d:%02d", hour, minute)
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
            ).onSuccess {
                setState { state.value.copy(isLoading = false) }
                postSideEffect(EditInterviewScheduleSideEffect.EditSuccess)
            }.onFailure {
                setState { state.value.copy(isLoading = false) }
                postSideEffect(EditInterviewScheduleSideEffect.EditFailed)
            }
        }
    }
}

internal sealed interface EditInterviewScheduleSideEffect {
    data object EditSuccess : EditInterviewScheduleSideEffect
    data object EditFailed : EditInterviewScheduleSideEffect
    data object NavigateBack : EditInterviewScheduleSideEffect
}
