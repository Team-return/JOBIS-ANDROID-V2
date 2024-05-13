package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "access_expires_at") val accessExpiresAt: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "refresh_expires_at") val refreshExpiresAt: String,
)
