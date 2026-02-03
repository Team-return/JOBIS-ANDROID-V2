package team.retum.network.model.request.interview

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.HiringProgress

@JsonClass(generateAdapter = true)
data class InterviewRequest(
    @Json(name = "interview_type") val interviewType: HiringProgress,
    @Json(name = "start_date") val startDate: String,
    @Json(name = "end_date") val endDate: String,
    @Json(name = "interview_time") val interviewTime: String,
    @Json(name = "company_name") val companyName: String,
    @Json(name = "location") val location: String,
    @Json(name = "student_id") val studentId: Long,
)
