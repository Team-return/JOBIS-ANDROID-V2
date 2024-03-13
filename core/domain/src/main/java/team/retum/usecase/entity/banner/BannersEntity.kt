package team.retum.usecase.entity.banner

import team.retum.common.enums.BannerType
import team.retum.network.model.response.banner.FetchBannersResponse

data class BannersEntity(
    val banners: List<BannerEntity>,
) {
    data class BannerEntity(
        val bannerId: String,
        val bannerUrl: String,
        val bannerType: BannerType,
    )
}

internal fun FetchBannersResponse.toEntity() = BannersEntity(
    banners = this.banners.map { it.toEntity() },
)

private fun FetchBannersResponse.BannerResponse.toEntity() = BannersEntity.BannerEntity(
    bannerId = this.bannerId,
    bannerUrl = this.bannerUrl,
    bannerType = this.bannerType,
)
