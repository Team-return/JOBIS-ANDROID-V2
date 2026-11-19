package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesCountResponse
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeApi {
    @GET(RequestUrls.Notice.notices)
    suspend fun fetchNotices(
        @Query("page") page: Long,
    ): FetchNoticesResponse

    @GET(RequestUrls.Notice.count)
    suspend fun fetchNoticesCount(): FetchNoticesCountResponse

    @GET(RequestUrls.Notice.details)
    suspend fun fetchNoticeDetails(
        @Path("notice-id") noticeId: Long,
    ): FetchNoticeDetailsResponse
}
