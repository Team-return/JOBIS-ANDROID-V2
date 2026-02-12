package team.retum.jobis.interview.schedule.ui

import android.accounts.AccountManager
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import team.retum.common.enums.HiringProgress
import team.retum.jobis.interview.schedule.ui.component.InterviewScheduleForm
import team.retum.jobis.interview.schedule.util.GoogleCalendarHelper
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
import kotlinx.coroutines.launch
@Composable
internal fun WriteInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    val writeInterviewScheduleViewModel: WriteInterviewScheduleViewModel = hiltViewModel()
    val state by writeInterviewScheduleViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleCalendarHelper = remember { GoogleCalendarHelper(context) }
    val scope = rememberCoroutineScope()

    val consentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            state.pendingCalendarEvent?.let { eventData ->
                scope.launch {
                    googleCalendarHelper.insertEvent(eventData)
                        .onSuccess {
                            JobisToast.create(
                                context = context,
                                message = context.getString(R.string.calendar_add_success),
                            ).show()
                        }
                        .onFailure { e ->
                            Log.e("GoogleCalendar", "Failed to insert event after consent", e)
                            JobisToast.create(
                                context = context,
                                message = context.getString(R.string.calendar_add_failed),
                                drawable = JobisIcon.Error,
                            ).show()
                        }
                    onBackPressed()
                }
            }
        } else {
            JobisToast.create(
                context = context,
                message = context.getString(R.string.add_success_message),
            ).show()
            onBackPressed()
        }
    }

    val accountPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        writeInterviewScheduleViewModel.dismissCalendarDialog()
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)?.let { accountName ->
                googleCalendarHelper.credential.selectedAccountName = accountName
                state.pendingCalendarEvent?.let { eventData ->
                    scope.launch {
                        googleCalendarHelper.insertEvent(eventData)
                            .onSuccess {
                                JobisToast.create(
                                    context = context,
                                    message = context.getString(R.string.calendar_add_success),
                                ).show()
                                onBackPressed()
                            }
                            .onFailure { e ->
                                if (e is UserRecoverableAuthIOException) {
                                    consentLauncher.launch(e.intent)
                                } else {
                                    JobisToast.create(
                                        context = context,
                                        message = context.getString(R.string.calendar_add_failed),
                                        drawable = JobisIcon.Error,
                                    ).show()
                                    onBackPressed()
                                }
                            }
                    }
                }
            }
        } else {
            JobisToast.create(
                context = context,
                message = context.getString(R.string.add_success_message),
            ).show()
            onBackPressed()
        }
    }

    LaunchedEffect(Unit) {
        writeInterviewScheduleViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is WriteInterviewScheduleSideEffect.AddSuccessRequestCalendar -> {
                    writeInterviewScheduleViewModel.showCalendarDialog(sideEffect.eventData)
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

    if (state.showCalendarDialog) {
        JobisDialog(
            onDismissRequest = {
                writeInterviewScheduleViewModel.dismissCalendarDialog()
                JobisToast.create(
                    context = context,
                    message = context.getString(R.string.add_success_message),
                ).show()
                onBackPressed()
            },
            title = stringResource(id = R.string.calendar_dialog_title),
            description = stringResource(id = R.string.calendar_dialog_description),
            subButtonText = stringResource(id = R.string.calendar_dialog_cancel),
            mainButtonText = stringResource(id = R.string.calendar_dialog_confirm),
            subButtonColor = ButtonColor.Default,
            mainButtonColor = ButtonColor.Primary,
            onSubButtonClick = {
                writeInterviewScheduleViewModel.dismissCalendarDialog()
                JobisToast.create(
                    context = context,
                    message = context.getString(R.string.add_success_message),
                ).show()
                onBackPressed()
            },
            onMainButtonClick = {
                accountPickerLauncher.launch(
                    googleCalendarHelper.credential.newChooseAccountIntent(),
                )
            },
        )
    }

    WriteInterviewScheduleScreen(
        onBackPressed = onBackPressed,
        state = state,
        onCompanyChange = writeInterviewScheduleViewModel::setCompany,
        onCompanySelected = writeInterviewScheduleViewModel::onCompanySelected,
        onDismissCompanySuggestions = writeInterviewScheduleViewModel::dismissCompanySuggestions,
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
    onCompanySelected: (Long, String) -> Unit,
    onDismissCompanySuggestions: () -> Unit,
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
                },
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
                companySuggestions = if (state.isEditMode) emptyList() else state.companySuggestions,
                showCompanySuggestions = if (state.isEditMode) false else state.showCompanySuggestions,
                onCompanySelected = onCompanySelected,
                onDismissCompanySuggestions = onDismissCompanySuggestions,
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
                },
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
                    .toEpochMilli(),
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
                        },
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
