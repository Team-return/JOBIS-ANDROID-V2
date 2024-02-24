package team.retum.signup.model

import java.io.Serializable

data class SignUpData(
    val email: String = "",
    val password: String = "",
    val grade: String = "",
    val name: String = "",
    val gender: String = "",
    val classRoom: String = "",
    val number: String = "",
    val platformType: String = "",
) : Serializable
