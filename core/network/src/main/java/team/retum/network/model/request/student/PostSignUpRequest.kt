package team.retum.network.model.request.student

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.Gender
import team.retum.common.enums.PlatformType

@JsonClass(generateAdapter = true)
data class PostSignUpRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "grade") val grade: String,
    @Json(name = "name") val name: String,
    @Json(name = "gender") val gender: Gender,
    @Json(name = "class_room") val classRoom: Long,
    @Json(name = "number") val number: Long,
    @Json(name = "profile_image_url") val profileImageUrl: String,
    @Json(name = "platform_type") val platformType: PlatformType,
    @Json(name = "device_token") val deviceToken: String,
)
