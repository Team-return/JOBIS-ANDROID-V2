package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookmarksResponse(
    @Json(name = "bookmarks") val bookmarks: List<BookmarkResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class BookmarkResponse(
        @Json(name = "company_logo_url") val companyLogoUrl: String,
        @Json(name = "company_name") val companyName: String,
        @Json(name = "recruitment_id") val recruitmentId: Long,
        @Json(name = "created_at") val createdAt: String,
    )
}
