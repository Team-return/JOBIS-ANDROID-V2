package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class BookmarksResponse(
    @SerializedName("bookmarks") val bookmarks: List<BookmarkResponse>
) {
    data class BookmarkResponse(
        @SerializedName("company_logo_url") val companyLogoUrl: String,
        @SerializedName("company_name") val companyName: String,
        @SerializedName("recruitment_id") val recruitmentId: Long,
        @SerializedName("created_at") val createdAt: String,
    )
}
