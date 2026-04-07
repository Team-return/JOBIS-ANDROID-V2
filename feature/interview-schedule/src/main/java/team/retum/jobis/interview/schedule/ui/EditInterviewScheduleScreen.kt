package team.retum.jobis.interview.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.interview.schedule.ui.component.InterviewScheduleForm
import team.retum.jobis.interview.schedule.viewmodel.DatePickerTarget
import team.retum.jobis.interview.schedule.viewmodel.EditInterviewScheduleSideEffect
import team.retum.jobis.interview.schedule.viewmodel.EditInterviewScheduleViewModel
import team.retum.jobis.interview_schedule.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.dialog.JobisDialog
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    val editInterviewScheduleViewModel: EditInterviewScheduleViewModel = hiltViewModel()
    val state by editInterviewScheduleViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        editInterviewScheduleViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is EditInterviewScheduleSideEffect.EditSuccess -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.edit_success_message),
                    ).show()
                    onBackPressed()
                }
                is EditInterviewScheduleSideEffect.EditFailed -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.edit_failed_message),
                        drawable = JobisIcon.Error,
                    ).show()
                }
                is EditInterviewScheduleSideEffect.NavigateBack -> {
                    onBackPressed()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.edit_interview_schedule),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            InterviewScheduleForm(
                company = state.company,
                onCompanyChange = {},
                companySuggestions = emptyList(),
                showCompanySuggestions = false,
                onCompanySelected = { _, _ -> },
                onDismissCompanySuggestions = {},
                location = state.location,
                onLocationChange = editInterviewScheduleViewModel::setLocation,
                hiringProgress = state.hiringProgress,
                showHiringProgressDropdown = state.showHiringProgressDropdown,
                onHiringProgressSelected = editInterviewScheduleViewModel::onHiringProgressSelected,
                onHiringProgressDropdownClick = editInterviewScheduleViewModel::onHiringProgressDropdownClick,
                startDate = state.startDate,
                endDate = state.endDate,
                isDateRange = state.isDateRange,
                onStartDateClick = editInterviewScheduleViewModel::onStartDateClick,
                onEndDateClick = editInterviewScheduleViewModel::onEndDateClick,
                onIsDateRangeChange = editInterviewScheduleViewModel::setIsDateRange,
                time = state.time,
                onTimeChange = editInterviewScheduleViewModel::setTime,
                isEditMode = true,
            )
        }
        JobisButton(
            text = stringResource(id = R.string.edit_button),
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
            onClick = editInterviewScheduleViewModel::onSaveClick,
        )
    }

    state.showDatePicker?.let { target ->
        key(target) {
            val initialDate = when (target) {
                DatePickerTarget.START -> state.startDate
                DatePickerTarget.END -> state.endDate
            }
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
            )
            DatePickerDialog(
                onDismissRequest = editInterviewScheduleViewModel::onDismissDatePicker,
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant
                                    .ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                when (target) {
                                    DatePickerTarget.START -> editInterviewScheduleViewModel.setStartDate(selectedDate)
                                    DatePickerTarget.END -> editInterviewScheduleViewModel.setEndDate(selectedDate)
                                }
                            }
                        },
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = editInterviewScheduleViewModel::onDismissDatePicker) {
                        Text(stringResource(id = R.string.cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }

    if (state.showConfirmDialog) {
        JobisDialog(
            onDismissRequest = editInterviewScheduleViewModel::onDismissConfirmDialog,
            title = stringResource(id = R.string.edit_confirm_title),
            description = stringResource(id = R.string.edit_confirm_description),
            subButtonText = stringResource(id = R.string.cancel),
            mainButtonText = stringResource(id = R.string.confirm),
            subButtonColor = ButtonColor.Default,
            mainButtonColor = ButtonColor.Primary,
            onSubButtonClick = editInterviewScheduleViewModel::onDismissConfirmDialog,
            onMainButtonClick = editInterviewScheduleViewModel::onConfirmEdit,
        )
    }
}
