package team.retum.network.model.response.interests

import com.squareup.moshi.Json

data class FetchInterestsRecruitmentResponse(
    @Json(name = "recruitments") val recruitments: List<InterestRecruitment>,
) {
    data class InterestRecruitment(
        @Json(name = "id") val id: Int,
        @Json(name = "company_id") val companyId: String,
        @Json(name = "company_profile_url") val companyProfileUrl: String,
        @Json(name = "train_pay") val trainPay: Int,
        @Json(name = "military_support") val militarySupport: Boolean,
        @Json(name = "hiring_jobs") val hiringJobs: String,
        @Json(name = "bookmarked") val bookmarked: Boolean,
    )
}
