package team.retum.network.model.response.interests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchInterestsRecruitmentsResponse(
    @Json(name = "recruitments") val recruitments: List<InterestRecruitments>,
) {
    data class InterestRecruitments(
        @Json(name = "id") val id: Int,
        @Json(name = "company_id") val companyId: String,
        @Json(name = "company_profile_url") val companyProfileUrl: String,
        @Json(name = "train_pay") val trainPay: Int,
        @Json(name = "military_support") val militarySupport: Boolean,
        @Json(name = "hiring_jobs") val hiringJobs: String,
        @Json(name = "bookmarked") val bookmarked: Boolean,
    )
}
