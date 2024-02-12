package team.retum.network.datasource

import team.retum.network.model.response.BookmarksResponse

interface BookmarkDataSource {
    suspend fun bookmarks(): BookmarksResponse

    suspend fun recruitmentBookmark(recruitmentId: Long)
}