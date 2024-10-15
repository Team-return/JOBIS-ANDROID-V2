package team.retum.jobis.local.datasource.device

interface LocalDeviceDataSource {
    suspend fun saveDeviceToken(deviceToken: String)

    suspend fun getDeviceToken(): String

    suspend fun clearDeviceToken()
}
