package team.retum.signup.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SignUpData(
    val email: String = "",
    val password: String = "",
    val grade: String = "",
    val name: String = "",
    val gender: String = "",
    val classRoom: String = "",
    val number: String = "",
    val platformType: String = "",
)

internal fun SignUpData.toJsonString() = Json.encodeToString(this)
internal fun String.toSignUpData() = Json.decodeFromString<SignUpData>(this)
