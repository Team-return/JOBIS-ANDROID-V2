package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchRecruitmentsResponse(
    @SerializedName("recruitments") val recruitments: List<RecruitmentResponse>,
) {
    data class RecruitmentResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("company_name") val companyName: String,
        @SerializedName("company_profile_url") val companyProfileUrl: String,
        @SerializedName("train_pay") val trainPay: Long,
        @SerializedName("military_support") val militarySupport: Boolean,
        @SerializedName("hiring_jobs") val hiringJobs: String,
        @SerializedName("bookmarked") val bookmarked: Boolean,
    )
}
