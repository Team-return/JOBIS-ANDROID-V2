package team.retum.network.datasource.code

import team.retum.common.enums.CodeType
import team.retum.network.api.CodeApi
import team.retum.network.model.response.FetchCodesResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteCodeDataSourceImpl @Inject constructor(
    private val codeApi: CodeApi,
) : RemoteCodeDataSource {
    override suspend fun fetchCodes(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ): FetchCodesResponse =
        RequestHandler<FetchCodesResponse>().request {
            codeApi.fetchCodes(
                keyword = keyword,
                type = type,
                parentCode = parentCode,
            )
        }
}
