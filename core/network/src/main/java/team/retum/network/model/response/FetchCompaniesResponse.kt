package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchCompaniesResponse(
    @SerializedName("companies") val companies: List<CompanyResponse>,
) {
    data class CompanyResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("logo_url") val logoUrl: String,
        @SerializedName("take") val take: Float,
        @SerializedName("has_recruitment") val hasRecruitment: Boolean,
    )
}
