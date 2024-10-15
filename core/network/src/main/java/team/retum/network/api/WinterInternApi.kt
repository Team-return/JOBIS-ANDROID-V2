package team.retum.network.api

import retrofit2.http.GET
import team.retum.network.di.RequestUrls

interface WinterInternApi {
    @GET(RequestUrls.WinterIntern.winterIntern)
    suspend fun fetchWinterInter(): Boolean
}
