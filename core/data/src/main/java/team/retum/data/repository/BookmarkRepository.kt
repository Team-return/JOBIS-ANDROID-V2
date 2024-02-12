package team.retum.data.repository

import team.retum.network.model.response.BookmarksResponse

interface BookmarkRepository {
    suspend fun bookmarks(): BookmarksResponse

    suspend fun recruitmentBookmark(recruitmentId: Long)
}