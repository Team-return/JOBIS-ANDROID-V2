package team.retum.network.datasource.banner

import team.retum.network.model.response.banner.FetchBannersResponse

interface RemoteBannerDataSource {
    suspend fun fetchBanners(): FetchBannersResponse
}
