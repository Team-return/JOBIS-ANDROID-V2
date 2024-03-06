package team.retum.common.utils

object Regex {
    const val PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*])[a-zA-Z\\d!@#\$%^&*]{8,16}\$"
    const val EMAIL = "^.+@dsm.hs.kr$"
}
