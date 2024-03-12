package team.retum.jobis.local.datasource.user

interface LocalUserDataSource {
    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveAccessExpiresAt(accessExpiresAt: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveRefreshExpiresAt(refreshExpiresAt: String)

    fun getAccessToken(): String

    fun getAccessExpiresAt(): String

    fun getRefreshToken(): String

    fun getRefreshExpiresAt(): String

    fun clearUserInformation()

    suspend fun saveDeviceToken(deviceToken: String)

    suspend fun getDeviceToken(): String
}
