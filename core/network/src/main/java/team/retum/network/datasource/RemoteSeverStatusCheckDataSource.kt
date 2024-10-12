package team.retum.network.datasource

interface RemoteSeverStatusCheckDataSource {
    suspend fun severStatusCheck()
}
