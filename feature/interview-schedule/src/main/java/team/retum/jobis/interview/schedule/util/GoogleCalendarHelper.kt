package team.retum.jobis.interview.schedule.util

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class CalendarEventData(
    val title: String,
    val description: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val interviewTime: String,
)

class GoogleCalendarHelper(context: Context) {

    val credential: GoogleAccountCredential = GoogleAccountCredential
        .usingOAuth2(context, listOf(CalendarScopes.CALENDAR_EVENTS))
        .setBackOff(ExponentialBackOff())

    private fun buildService(): Calendar {
        return Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential,
        )
            .setApplicationName("JOBIS")
            .build()
    }

    suspend fun insertEvent(eventData: CalendarEventData): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val service = buildService()

                val startTime = LocalTime.parse(
                    eventData.interviewTime,
                    DateTimeFormatter.ofPattern("HH:mm"),
                )
                val zoneId = ZoneId.of("Asia/Seoul")
                val startLocalDateTime = LocalDate.parse(eventData.startDate).atTime(startTime)
                val endLocalDateTime = startLocalDateTime.plusHours(1)

                val startDateTime = startLocalDateTime.atZone(zoneId)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val endDateTime = endLocalDateTime.atZone(zoneId)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                val event = Event().apply {
                    summary = eventData.title
                    description = eventData.description
                    this.location = eventData.location
                    start = EventDateTime().apply {
                        dateTime = com.google.api.client.util.DateTime(startDateTime)
                        timeZone = "Asia/Seoul"
                    }
                    end = EventDateTime().apply {
                        dateTime = com.google.api.client.util.DateTime(endDateTime)
                        timeZone = "Asia/Seoul"
                    }
                }

                val createdEvent = service.events()
                    .insert("primary", event)
                    .execute()

                createdEvent.id
            }
        }
}
