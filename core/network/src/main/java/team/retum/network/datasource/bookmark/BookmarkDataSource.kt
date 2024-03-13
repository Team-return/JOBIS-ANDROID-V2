package team.retum.network.datasource.bookmark

import team.retum.network.model.response.BookmarksResponse

interface BookmarkDataSource {
    suspend fun fetchBookmark(): BookmarksResponse

    suspend fun bookmarkRecruitment(recruitmentId: Long)
}
