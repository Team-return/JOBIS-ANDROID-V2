package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.BookmarksResponse

interface BookmarkApi {
    @GET(RequestUrls.Bookmarks.bookmarks)
    suspend fun fetchBookmarks(): BookmarksResponse

    @PATCH(RequestUrls.Bookmarks.bookmark)
    suspend fun bookmarkRecruitment(
        @Path("recruitment-id") recruitmentId: Long,
    )
}