package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeApi {
    @GET(RequestUrls.Notice.notices)
    suspend fun fetchNotices(): FetchNoticesResponse

    @GET(RequestUrls.Notice.details)
    suspend fun fetchNoticeDetails(
        @Path("notice-id") noticeId: Long,
    ): FetchNoticeDetailsResponse
}
