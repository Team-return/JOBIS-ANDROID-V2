package team.retum.usecase.entity

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.BookmarksResponse

data class BookmarksEntity(
    val bookmarks: List<BookmarkEntity>,
) {
    data class BookmarkEntity(
        val companyLogoUrl: String,
        val companyName: String,
        val recruitmentId: Long,
        val createdAt: String,
    )
}

fun BookmarksResponse.toBookmarkEntity() = BookmarksEntity(
    bookmarks = this.bookmarks.map { it.toEntity() },
)

private fun BookmarksResponse.BookmarkResponse.toEntity() = BookmarksEntity.BookmarkEntity(
    companyLogoUrl = ResourceKeys.IMAGE_URL + this.companyLogoUrl,
    companyName = this.companyName,
    recruitmentId = this.recruitmentId,
    createdAt = this.createdAt.substring(0..9),
)
