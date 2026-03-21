package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)

data class RecentResponse(
    val companies: List<RecentCompanyResponse>,
)

@JsonClass(generateAdapter = true)
data class RecentCompanyResponse(
    @Json(name = "company_id") val companyId: Long,
    @Json(name = "company_name") val companyName: String,
    @Json(name = "company_logo_url") val companyLogoUrl: String,
    @Json(name = "is_recruiting") val isRecruiting: Boolean,
)
