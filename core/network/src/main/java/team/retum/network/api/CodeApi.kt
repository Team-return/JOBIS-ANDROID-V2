package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.common.enums.CodeType
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.FetchCodesResponse

interface CodeApi {
    @GET(RequestUrls.Codes.codes)
    suspend fun fetchCodes(
        @Query("keyword") keyword: String?,
        @Query("type") type: CodeType,
        @Query("parent_code") parentCode: Long?,
    ): FetchCodesResponse
}
