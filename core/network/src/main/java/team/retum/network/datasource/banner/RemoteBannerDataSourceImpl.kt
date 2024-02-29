package team.retum.network.datasource.banner

import team.retum.network.api.BannerApi
import team.retum.network.model.response.banner.FetchBannersResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteBannerDataSourceImpl @Inject constructor(
    private val bannerApi: BannerApi,
) : RemoteBannerDataSource {
    override suspend fun fetchBanners(): FetchBannersResponse {
        return RequestHandler<FetchBannersResponse>().request {
            bannerApi.fetchBanners()
        }
    }
}
