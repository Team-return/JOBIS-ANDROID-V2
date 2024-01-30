package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("access_expires_at") val accessExpiresAt: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("refresh_expires_at") val refreshExpiresAt: String,
)
