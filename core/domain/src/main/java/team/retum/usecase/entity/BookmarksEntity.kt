package team.retum.usecase.entity

import androidx.compose.runtime.Immutable
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.network.model.response.BookmarksResponse

data class BookmarksEntity(
    val bookmarks: List<BookmarkEntity>,
) {
    @Immutable
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
    companyLogoUrl = normalizeLogoUrl(this.companyLogoUrl),
    companyName = this.companyName,
    recruitmentId = this.recruitmentId,
    createdAt = if (createdAt.length >= 10) createdAt.substring(0..9) else createdAt,
)

fun List<BookmarkLocalEntity>.toBookmarkEntities() = map { it.toLocalEntity() }

private fun BookmarkLocalEntity.toLocalEntity() = BookmarksEntity.BookmarkEntity(
    companyLogoUrl = normalizeLogoUrl(companyLogoUrl),
    companyName = companyName,
    recruitmentId = recruitmentId,
    createdAt = if (createdAt.length >= 10) createdAt.substring(0..9) else createdAt,
)

private fun normalizeLogoUrl(logoUrl: String): String {
    return if (logoUrl.startsWith(ResourceKeys.IMAGE_URL)) {
        logoUrl
    } else {
        ResourceKeys.IMAGE_URL + logoUrl
    }
}
