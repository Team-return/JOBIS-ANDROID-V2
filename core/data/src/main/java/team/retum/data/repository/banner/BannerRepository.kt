package team.retum.data.repository.banner

import team.retum.network.model.response.banner.FetchBannersResponse

interface BannerRepository {
    suspend fun fetchBanners(): FetchBannersResponse
}
