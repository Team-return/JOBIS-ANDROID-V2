package team.retum.network.model.request.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.AuthCodeType

@JsonClass(generateAdapter = true)
data class SendAuthenticationCodeRequest(
    @Json(name = "email") val email: String,
    @Json(name = "auth_code_type") val authCodeType: AuthCodeType,
)
