package team.retum.network.model.request.user

import com.google.gson.annotations.SerializedName

data class RegisterDeviceTokenRequest(
    @SerializedName("token") val deviceToken: String,
)
