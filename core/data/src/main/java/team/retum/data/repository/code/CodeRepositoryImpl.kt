package team.retum.data.repository.code

import team.retum.common.enums.CodeType
import team.retum.network.datasource.code.RemoteCodeDataSource
import team.retum.network.model.response.FetchCodesResponse
import javax.inject.Inject

class CodeRepositoryImpl @Inject constructor(
    private val codeDataSource: RemoteCodeDataSource,
) : CodeRepository {
    override suspend fun fetchCodes(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ): FetchCodesResponse =
        codeDataSource.fetchCodes(
            keyword = keyword,
            type = type,
            parentCode = parentCode,
        )

}
