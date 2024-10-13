package team.retum.network.datasource

interface RemoteServerStatusCheckDataSource {
    suspend fun serverStatusCheck()
}
