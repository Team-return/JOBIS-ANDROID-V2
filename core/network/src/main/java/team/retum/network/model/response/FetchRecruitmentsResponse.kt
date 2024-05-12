package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchRecruitmentsResponse(
    @Json(name = "recruitments") val recruitments: List<RecruitmentResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class RecruitmentResponse(
        @Json(name = "id") val id: Long,
        @Json(name = "company_name") val companyName: String,
        @Json(name = "company_profile_url") val companyProfileUrl: String,
        @Json(name = "train_pay") val trainPay: Long,
        @Json(name = "military_support") val militarySupport: Boolean,
        @Json(name = "hiring_jobs") val hiringJobs: String,
        @Json(name = "bookmarked") val bookmarked: Boolean,
    )
}
