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
import kotlinx.coroutines.launch
import team.retum.jobis.interview.schedule.ui.component.InterviewScheduleForm
import team.retum.jobis.interview.schedule.util.GoogleCalendarHelper
import team.retum.jobis.interview.schedule.viewmodel.AddInterviewScheduleSideEffect
import team.retum.jobis.interview.schedule.viewmodel.AddInterviewScheduleViewModel
import team.retum.jobis.interview.schedule.viewmodel.DatePickerTarget
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
internal fun AddInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    val addInterviewScheduleViewModel: AddInterviewScheduleViewModel = hiltViewModel()
    val state by addInterviewScheduleViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleCalendarHelper = remember { GoogleCalendarHelper(context) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

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
        addInterviewScheduleViewModel.dismissCalendarDialog()
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
        addInterviewScheduleViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is AddInterviewScheduleSideEffect.AddSuccessRequestCalendar -> {
                    addInterviewScheduleViewModel.showCalendarDialog(sideEffect.eventData)
                }
                is AddInterviewScheduleSideEffect.AddFailed -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.add_failed_message),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    if (state.showCalendarDialog) {
        JobisDialog(
            onDismissRequest = {
                addInterviewScheduleViewModel.dismissCalendarDialog()
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
                addInterviewScheduleViewModel.dismissCalendarDialog()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.add_interview_schedule),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            InterviewScheduleForm(
                company = state.company,
                onCompanyChange = addInterviewScheduleViewModel::setCompany,
                companySuggestions = state.companySuggestions,
                showCompanySuggestions = state.showCompanySuggestions,
                onCompanySelected = addInterviewScheduleViewModel::onCompanySelected,
                onDismissCompanySuggestions = addInterviewScheduleViewModel::dismissCompanySuggestions,
                location = state.location,
                onLocationChange = addInterviewScheduleViewModel::setLocation,
                hiringProgress = state.hiringProgress,
                showHiringProgressDropdown = state.showHiringProgressDropdown,
                onHiringProgressSelected = addInterviewScheduleViewModel::onHiringProgressSelected,
                onHiringProgressDropdownClick = addInterviewScheduleViewModel::onHiringProgressDropdownClick,
                startDate = state.startDate,
                endDate = state.endDate,
                isDateRange = state.isDateRange,
                onStartDateClick = addInterviewScheduleViewModel::onStartDateClick,
                onEndDateClick = addInterviewScheduleViewModel::onEndDateClick,
                onIsDateRangeChange = addInterviewScheduleViewModel::setIsDateRange,
                time = state.time,
                onTimeChange = addInterviewScheduleViewModel::setTime,
                isEditMode = false,
            )
        }
        JobisButton(
            text = stringResource(id = R.string.add_button),
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
            onClick = addInterviewScheduleViewModel::onSaveClick,
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
                onDismissRequest = addInterviewScheduleViewModel::onDismissDatePicker,
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant
                                    .ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                when (target) {
                                    DatePickerTarget.START -> addInterviewScheduleViewModel.setStartDate(selectedDate)
                                    DatePickerTarget.END -> addInterviewScheduleViewModel.setEndDate(selectedDate)
                                }
                            }
                        },
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = addInterviewScheduleViewModel::onDismissDatePicker) {
                        Text(stringResource(id = R.string.cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
