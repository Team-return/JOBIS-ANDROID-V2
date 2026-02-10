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
import team.retum.jobis.interview.schedule.viewmodel.WriteInterviewScheduleSideEffect
import team.retum.jobis.interview.schedule.viewmodel.WriteInterviewScheduleState
import team.retum.jobis.interview.schedule.viewmodel.WriteInterviewScheduleViewModel
import team.retum.jobis.interview_schedule.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.dialog.JobisDialog
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import team.retum.common.enums.HiringProgress

@Composable
internal fun WriteInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    val writeInterviewScheduleViewModel: WriteInterviewScheduleViewModel = hiltViewModel()
    val state by writeInterviewScheduleViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        writeInterviewScheduleViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is WriteInterviewScheduleSideEffect.AddSuccess -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.add_success_message),
                    ).show()
                    onBackPressed()
                }
                is WriteInterviewScheduleSideEffect.AddFailed -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.add_failed_message),
                        drawable = JobisIcon.Error,
                    ).show()
                }
                is WriteInterviewScheduleSideEffect.EditSuccess -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.edit_success_message),
                    ).show()
                    onBackPressed()
                }
                is WriteInterviewScheduleSideEffect.EditFailed -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.edit_failed_message),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    WriteInterviewScheduleScreen(
        onBackPressed = onBackPressed,
        state = state,
        onCompanyChange = writeInterviewScheduleViewModel::setCompany,
        onLocationChange = writeInterviewScheduleViewModel::setLocation,
        onHiringProgressSelected = writeInterviewScheduleViewModel::onHiringProgressSelected,
        onHiringProgressDropdownClick = writeInterviewScheduleViewModel::onHiringProgressDropdownClick,
        onStartDateClick = writeInterviewScheduleViewModel::onStartDateClick,
        onEndDateClick = writeInterviewScheduleViewModel::onEndDateClick,
        onIsDateRangeChange = writeInterviewScheduleViewModel::setIsDateRange,
        onTimeChange = writeInterviewScheduleViewModel::setTime,
        onSaveClick = writeInterviewScheduleViewModel::onSaveClick,
        onDateSelected = { target, date ->
            when (target) {
                DatePickerTarget.START -> writeInterviewScheduleViewModel.setStartDate(date)
                DatePickerTarget.END -> writeInterviewScheduleViewModel.setEndDate(date)
            }
        },
        onDismissDatePicker = writeInterviewScheduleViewModel::onDismissDatePicker,
        onConfirmEdit = writeInterviewScheduleViewModel::onConfirmEdit,
        onDismissConfirmDialog = writeInterviewScheduleViewModel::onDismissConfirmDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WriteInterviewScheduleScreen(
    onBackPressed: () -> Unit,
    state: WriteInterviewScheduleState,
    onCompanyChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onHiringProgressSelected: (HiringProgress) -> Unit,
    onHiringProgressDropdownClick: () -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onIsDateRangeChange: (Boolean) -> Unit,
    onTimeChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDateSelected: (DatePickerTarget, LocalDate) -> Unit,
    onDismissDatePicker: () -> Unit,
    onConfirmEdit: () -> Unit,
    onDismissConfirmDialog: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(
                id = if (state.isEditMode) {
                    R.string.edit_interview_schedule
                } else {
                    R.string.add_interview_schedule
                }
            ),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            InterviewScheduleForm(
                company = state.company,
                onCompanyChange = if (state.isEditMode) { _ -> } else onCompanyChange,
                location = state.location,
                onLocationChange = onLocationChange,
                hiringProgress = state.hiringProgress,
                showHiringProgressDropdown = state.showHiringProgressDropdown,
                onHiringProgressSelected = onHiringProgressSelected,
                onHiringProgressDropdownClick = onHiringProgressDropdownClick,
                startDate = state.startDate,
                endDate = state.endDate,
                isDateRange = state.isDateRange,
                onStartDateClick = onStartDateClick,
                onEndDateClick = onEndDateClick,
                onIsDateRangeChange = onIsDateRangeChange,
                time = state.time,
                onTimeChange = onTimeChange,
                isEditMode = state.isEditMode,
            )
        }
        JobisButton(
            text = stringResource(
                id = if (state.isEditMode) {
                    R.string.edit_button
                } else {
                    R.string.add_button
                }
            ),
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
            onClick = onSaveClick,
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
                    .toEpochMilli()
            )

            DatePickerDialog(
                onDismissRequest = onDismissDatePicker,
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant
                                    .ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                onDateSelected(target, selectedDate)
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDatePicker) {
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
            onDismissRequest = onDismissConfirmDialog,
            title = stringResource(id = R.string.edit_confirm_title),
            description = stringResource(id = R.string.edit_confirm_description),
            subButtonText = stringResource(id = R.string.cancel),
            mainButtonText = stringResource(id = R.string.confirm),
            subButtonColor = ButtonColor.Default,
            mainButtonColor = ButtonColor.Primary,
            onSubButtonClick = onDismissConfirmDialog,
            onMainButtonClick = onConfirmEdit,
        )
    }
}
