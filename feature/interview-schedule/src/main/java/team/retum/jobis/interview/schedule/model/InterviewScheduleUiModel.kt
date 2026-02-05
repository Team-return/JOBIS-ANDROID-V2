package team.retum.jobis.interview.schedule.model

import androidx.compose.runtime.Immutable
import team.retum.usecase.entity.interview.InterviewsEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Immutable
data class InterviewScheduleUiModel(
    val id: Long,
    val companyName: String,
    val interviewType: String,
    val location: String,
    val startDate: String,
    val endDate: String?,
    val interviewTime: String,
    val isSelected: Boolean = false,
)

fun InterviewsEntity.InterviewEntity.toUiModel(isSelected: Boolean = false): InterviewScheduleUiModel {
    return InterviewScheduleUiModel(
        id = id,
        companyName = companyName,
        interviewType = interviewType.value,
        location = location,
        startDate = formatDate(startDate),
        endDate = if (endDate.isNotEmpty()) formatDate(endDate) else null,
        interviewTime = formatTime(interviewTime),
        isSelected = isSelected,
    )
}

private fun formatDate(dateString: String): String {
    return runCatching {
        val date = LocalDate.parse(dateString)
        date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }.getOrDefault(dateString)
}

private fun formatTime(timeString: String): String {
    return runCatching {
        val time = LocalTime.parse(timeString)
        val hour = time.hour
        val minute = time.minute
        val period = if (hour < 12) "오전" else "오후"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        if (minute == 0) {
            "$period ${displayHour}시"
        } else {
            "$period ${displayHour}시 ${minute}분"
        }
    }.getOrDefault(timeString)
}
