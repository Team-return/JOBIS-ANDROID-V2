package team.retum.network.model.response.banner

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.BannerType

data class FetchBannersResponse(
    @SerializedName("banners") val banners: List<BannerResponse>,
) {
    data class BannerResponse(
        @SerializedName("id") val bannerId: String,
        @SerializedName("banner_url") val bannerUrl: String,
        @SerializedName("banner_type") val bannerType: BannerType,
    )
}
