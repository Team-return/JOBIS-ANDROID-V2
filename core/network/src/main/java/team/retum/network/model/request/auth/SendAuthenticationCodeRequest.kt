package team.retum.network.model.request.auth

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.AuthCodeType

data class SendAuthenticationCodeRequest(
    @SerializedName("email") val email: String,
    @SerializedName("auth_code_type") val authCodeType: AuthCodeType,
)
