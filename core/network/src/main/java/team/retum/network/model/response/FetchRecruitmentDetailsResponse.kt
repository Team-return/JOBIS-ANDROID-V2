package team.retum.network.model.response

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.HiringProgress

data class FetchRecruitmentDetailsResponse(
    @SerializedName("company_id") val companyId: Long,
    @SerializedName("company_profile_url") val companyProfileUrl: String,
    @SerializedName("company_name") val companyName: String,
    @SerializedName("areas") val areas: List<Areas>,
    @SerializedName("required_grade") val requiredGrade: Long?,
    @SerializedName("working_hours") val workingHours: String,
    @SerializedName("required_licenses") val requiredLicenses: List<String>?,
    @SerializedName("hiring_progress") val hiringProgress: List<HiringProgress>,
    @SerializedName("train_pay") val trainPay: Long,
    @SerializedName("pay") val pay: String?,
    @SerializedName("benefits") val benefits: String?,
    @SerializedName("military") val military: Boolean,
    @SerializedName("submit_document") val submitDocument: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("etc") val etc: String?,
    @SerializedName("is_applicable") val isApplicable: Boolean,
    @SerializedName("bookmarked") val bookmarked: Boolean,
)

data class Areas(
    @SerializedName("id") val id: Long,
    @SerializedName("job") val job: List<String>,
    @SerializedName("tech") val tech: List<String>,
    @SerializedName("hiring") val hiring: Long,
    @SerializedName("major_task") val majorTask: String,
    @SerializedName("preferential_treatment") val preferentialTreatment: String?,
)
