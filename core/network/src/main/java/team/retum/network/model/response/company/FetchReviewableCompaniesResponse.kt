package team.retum.network.model.response.company

import com.google.gson.annotations.SerializedName

data class FetchReviewableCompaniesResponse(
    @SerializedName("companies") val companies: List<CompanyResponse>,
) {
    data class CompanyResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
    )
}
