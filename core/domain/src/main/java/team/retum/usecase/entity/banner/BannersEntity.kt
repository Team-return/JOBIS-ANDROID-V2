package team.retum.usecase.entity.banner

import androidx.compose.runtime.Immutable
import team.retum.common.enums.BannerType
import team.retum.network.model.response.banner.FetchBannersResponse

data class BannersEntity(
    val banners: List<BannerEntity>,
) {
    @Immutable
    data class BannerEntity(
        val bannerId: Long,
        val title: String,
        val content: String,
        val bannerType: BannerType,
        val detailId: Long,
    )
}

internal fun FetchBannersResponse.toEntity() = BannersEntity(
    banners = this.banners.map { it.toEntity() },
)

private fun FetchBannersResponse.BannerResponse.toEntity() = BannersEntity.BannerEntity(
    bannerId = this.bannerId,
    title = this.title.orEmpty(),
    content = this.content.orEmpty(),
    bannerType = this.bannerType,
    detailId = this.detailId,
)
