package team.retum.data.repository

import team.retum.network.datasource.RemoteServerStatusCheckDataSource
import javax.inject.Inject

class ServerStatusCheckRepositoryImpl @Inject constructor(
    private val remoteServerStatusCheckDataSource: RemoteServerStatusCheckDataSource,
) : ServerStatusCheckRepository {
    override suspend fun serverStatusCheck() {
        remoteServerStatusCheckDataSource.serverStatusCheck()
    }
}
