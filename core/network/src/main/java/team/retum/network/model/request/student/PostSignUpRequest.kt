package team.retum.network.model.request.student

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.Gender
import team.retum.common.enums.PlatformType

data class PostSignUpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("grade") val grade: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: Gender,
    @SerializedName("class_room") val classRoom: Long,
    @SerializedName("number") val number: Long,
    @SerializedName("profile_image_url") val profileImageUrl: String,
    @SerializedName("platform_type") val platformType: PlatformType,
    @SerializedName("device_token") val deviceToken: String,
)
