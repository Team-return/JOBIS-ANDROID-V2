package team.retum.data.repository.code

import team.retum.common.enums.CodeType
import team.retum.network.model.response.FetchCodesResponse

interface CodeRepository {
    suspend fun fetchCodes(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ): FetchCodesResponse
}
