package team.retum.network.model.request.student

import com.google.gson.annotations.SerializedName

data class EditProfileImageRequest(
    @SerializedName("profile_image_url") val profileImageUrl: String,
)
