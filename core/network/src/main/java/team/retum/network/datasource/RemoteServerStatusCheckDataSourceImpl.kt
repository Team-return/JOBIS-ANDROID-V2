package team.retum.network.datasource

import team.retum.network.api.ServerStatusCheckApi
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteServerStatusCheckDataSourceImpl @Inject constructor(
    private val serverStatusCheckApi: ServerStatusCheckApi,
) : RemoteServerStatusCheckDataSource {
    override suspend fun serverStatusCheck() {
        RequestHandler<Unit>().request {
            serverStatusCheckApi.serverStatusCheck()
        }
    }
}
