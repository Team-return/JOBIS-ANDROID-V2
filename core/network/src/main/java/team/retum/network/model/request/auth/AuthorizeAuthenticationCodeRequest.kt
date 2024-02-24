package team.retum.network.model.request.auth

import com.google.gson.annotations.SerializedName

data class AuthorizeAuthenticationCodeRequest(
    @SerializedName("email") val email: String,
    @SerializedName("auth_code") val authCode: String,
)
