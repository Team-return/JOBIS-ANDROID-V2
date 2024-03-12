package team.retum.network.model.request.user

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.PlatformType

data class SignInRequest(
    @SerializedName("account_id") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("platform_type") val platformType: PlatformType = PlatformType.ANDROID,
)
