package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchCompaniesResponse(
    @Json(name = "companies") val companies: List<CompanyResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class CompanyResponse(
        @Json(name = "id") val id: Long,
        @Json(name = "name") val name: String,
        @Json(name = "logo_url") val logoUrl: String,
        @Json(name = "take") val take: Float,
        @Json(name = "has_recruitment") val hasRecruitment: Boolean,
    )
}
