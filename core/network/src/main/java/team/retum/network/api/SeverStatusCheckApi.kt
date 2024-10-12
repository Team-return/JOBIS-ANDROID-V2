package team.retum.network.api

import retrofit2.http.GET
import team.retum.network.di.RequestUrls

interface SeverStatusCheckApi {
    @GET(RequestUrls.CheckSeverStatus.checkSeverStatus)
    suspend fun severStatusCheck()
}
