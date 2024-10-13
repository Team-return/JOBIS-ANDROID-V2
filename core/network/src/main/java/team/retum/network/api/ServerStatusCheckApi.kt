package team.retum.network.api

import retrofit2.http.GET
import team.retum.network.di.RequestUrls

interface ServerStatusCheckApi {
    @GET(RequestUrls.CheckServerStatus.checkServerStatus)
    suspend fun serverStatusCheck()
}
