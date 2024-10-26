package team.retum.data.repository.user

import team.retum.network.model.request.user.SignInRequest
import team.retum.network.model.response.TokenResponse

interface UserRepository {
    suspend fun signIn(signInRequest: SignInRequest): TokenResponse

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveAccessExpiresAt(accessExpiresAt: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveRefreshExpiresAt(refreshExpiresAt: String)

    fun getAccessToken(): String

    fun getAccessExpiresAt(): String

    fun getRefreshToken(): String

    fun getRefreshExpiresAt(): String

    suspend fun signOut()

    suspend fun saveDeviceToken(deviceToken: String)

    suspend fun getDeviceToken(): String
}
