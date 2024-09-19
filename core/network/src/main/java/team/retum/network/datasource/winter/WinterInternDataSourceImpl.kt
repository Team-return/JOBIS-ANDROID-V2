package team.retum.network.datasource.winter

import team.retum.network.api.WinterInternApi
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class WinterInternDataSourceImpl @Inject constructor(
    private val winterInternApi: WinterInternApi,
) : WinterInternDataSource {
    override suspend fun fetchWinterIntern(): Boolean {
        return RequestHandler<Boolean>().request {
            winterInternApi.fetchWinterInter()
        }
    }
}
