package team.retum.network.model.request.student

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditProfileImageRequest(
    @Json(name = "profile_image_url") val profileImageUrl: String,
)
