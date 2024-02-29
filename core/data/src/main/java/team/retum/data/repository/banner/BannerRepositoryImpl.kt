package team.retum.data.repository.banner

import team.retum.network.datasource.banner.RemoteBannerDataSource
import team.retum.network.model.response.banner.FetchBannersResponse
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val remoteBannerDataSource: RemoteBannerDataSource,
): BannerRepository {
    override suspend fun fetchBanners(): FetchBannersResponse {
        return remoteBannerDataSource.fetchBanners()
    }
}
