package team.retum.signup.model

import java.io.Serializable

data class SignUpData(
    val email: String = "",
    val password: String = "",
    val grade: String = "",
    val name: String = "",
    val gender: String = "",
    val classRoom: Long = 0L,
    val number: Long = 0L,
    val platformType: String = "",
) : Serializable
