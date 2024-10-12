package team.retum.network.datasource

import team.retum.network.api.SeverStatusCheckApi
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteSeverStatusCheckDataSourceImpl @Inject constructor(
    private val severStatusCheckApi: SeverStatusCheckApi,
) : RemoteSeverStatusCheckDataSource{
    override suspend fun severStatusCheck() {
        RequestHandler<Unit>().request {
            severStatusCheckApi.severStatusCheck()
        }
    }
}
