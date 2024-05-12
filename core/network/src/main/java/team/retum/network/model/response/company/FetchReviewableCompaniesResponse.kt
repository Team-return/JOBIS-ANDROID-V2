package team.retum.network.model.response.company

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchReviewableCompaniesResponse(
    @Json(name = "companies") val companies: List<CompanyResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class CompanyResponse(
        @Json(name = "id") val id: Long,
        @Json(name = "name") val name: String,
    )
}
