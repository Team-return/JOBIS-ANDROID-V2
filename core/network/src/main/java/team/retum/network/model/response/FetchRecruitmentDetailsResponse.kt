package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.HiringProgress

@JsonClass(generateAdapter = true)
data class FetchRecruitmentDetailsResponse(
    @Json(name = "company_id") val companyId: Long,
    @Json(name = "company_profile_url") val companyProfileUrl: String,
    @Json(name = "company_name") val companyName: String,
    @Json(name = "areas") val areas: List<Areas>,
    @Json(name = "required_grade") val requiredGrade: Long?,
    @Json(name = "working_hours") val workingHours: String,
    @Json(name = "required_licenses") val requiredLicenses: List<String>?,
    @Json(name = "hiring_progress") val hiringProgress: List<HiringProgress>,
    @Json(name = "train_pay") val trainPay: Long,
    @Json(name = "pay") val pay: String?,
    @Json(name = "benefits") val benefits: String?,
    @Json(name = "military") val military: Boolean,
    @Json(name = "submit_document") val submitDocument: String,
    @Json(name = "start_date") val startDate: String?,
    @Json(name = "end_date") val endDate: String?,
    @Json(name = "etc") val etc: String?,
    @Json(name = "is_applicable") val isApplicable: Boolean,
    @Json(name = "bookmarked") val bookmarked: Boolean,
)

@JsonClass(generateAdapter = true)
data class Areas(
    @Json(name = "id") val id: Long,
    @Json(name = "job") val job: List<Job>,
    @Json(name = "tech") val tech: List<Tech>,
    @Json(name = "hiring") val hiring: Long,
    @Json(name = "major_task") val majorTask: String,
    @Json(name = "preferential_treatment") val preferentialTreatment: String?,
)

@JsonClass(generateAdapter = true)
data class Job(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
)

@JsonClass(generateAdapter = true)
data class Tech(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
)
