package team.retum.network.model.request.student

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForgottenPasswordRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
)
