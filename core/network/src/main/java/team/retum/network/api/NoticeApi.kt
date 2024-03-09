package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeApi {
    @GET(RequestUrls.Notice.notices)
    suspend fun fetchNotices(
        @Query("page") page: Int,
    ) : FetchNoticesResponse
}
