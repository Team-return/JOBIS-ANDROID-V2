package team.retum.network.model.response.banner

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.BannerType

@JsonClass(generateAdapter = true)
data class FetchBannersResponse(
    @Json(name = "banners") val banners: List<BannerResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class BannerResponse(
        @Json(name = "id") val bannerId: Long,
        @Json(name = "title") val title: String?,
        @Json(name = "content") val content: String?,
        @Json(name = "banner_type") val bannerType: BannerType,
        @Json(name = "detail_id") val detailId: Long,
    )
}
