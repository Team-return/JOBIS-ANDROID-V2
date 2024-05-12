package team.retum.network.model.response.banner

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.BannerType

@JsonClass(generateAdapter = true)
data class FetchBannersResponse(
    @Json(name = "banners") val banners: List<BannerResponse>,
) {
    data class BannerResponse(
        @Json(name = "id") val bannerId: String,
        @Json(name = "banner_url") val bannerUrl: String,
        @Json(name = "banner_type") val bannerType: BannerType,
    )
}
