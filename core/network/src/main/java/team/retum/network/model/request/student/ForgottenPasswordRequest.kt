package team.retum.network.model.request.student

import com.google.gson.annotations.SerializedName

data class ForgottenPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)
