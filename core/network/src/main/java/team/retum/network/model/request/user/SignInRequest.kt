package team.retum.network.model.request.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.PlatformType

@JsonClass(generateAdapter = true)
data class SignInRequest(
    @Json(name = "account_id") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "platform_type") val platformType: PlatformType = PlatformType.ANDROID,
    @Json(name = "device_token") val deviceToken: String,
)
