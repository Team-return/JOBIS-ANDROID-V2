package team.retum.data.repository

import team.retum.network.model.response.BookmarksResponse

interface BookmarkRepository {
    suspend fun fetchBookmarks(): BookmarksResponse

    suspend fun bookmarkRecruitment(recruitmentId: Long)
}