package team.retum.network.datasource.code

import team.retum.common.enums.CodeType
import team.retum.network.model.response.FetchCodesResponse

interface RemoteCodeDataSource {
    suspend fun fetchCodes(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ): FetchCodesResponse
}
