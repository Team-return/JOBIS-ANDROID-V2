package team.retum.network.api

import retrofit2.http.GET
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.banner.FetchBannersResponse

interface BannerApi {
    @GET(RequestUrls.Banner.banners)
    suspend fun fetchBanners(): FetchBannersResponse
}
