package team.retum.data.repository.code

import team.retum.common.enums.CodeType
import team.retum.jobis.local.datasource.code.LocalCodeDataSource
import team.retum.jobis.local.entity.CodeLocalEntity
import team.retum.network.datasource.code.RemoteCodeDataSource
import team.retum.network.model.response.FetchCodesResponse
import javax.inject.Inject

private const val TTL_MILLIS = 24 * 60 * 60 * 1000L

class CodeRepositoryImpl @Inject constructor(
    private val remoteCodeDataSource: RemoteCodeDataSource,
    private val localCodeDataSource: LocalCodeDataSource,
) : CodeRepository {

    override suspend fun fetchCodes(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ): FetchCodesResponse {
        if (keyword != null || type == CodeType.BUSINESS_AREA) {
            return remoteCodeDataSource.fetchCodes(
                keyword = keyword,
                type = type,
                parentCode = parentCode,
            )
        }

        val cached = localCodeDataSource.getCodes(
            type = type.name,
            parentCode = parentCode,
        )
        val now = System.currentTimeMillis()

        if (cached.isNotEmpty() && now - cached.first().cachedAt < TTL_MILLIS) {
            return FetchCodesResponse(
                codes = cached.map { FetchCodesResponse.CodeResponse(it.code, it.keyword) },
            )
        }

        val response = runCatching {
            remoteCodeDataSource.fetchCodes(
                keyword = null,
                type = type,
                parentCode = parentCode,
            )
        }.getOrElse {
            if (cached.isNotEmpty()) {
                return FetchCodesResponse(
                    codes = cached.map { FetchCodesResponse.CodeResponse(it.code, it.keyword) },
                )
            }
            throw it
        }

        localCodeDataSource.deleteByGroup(
            type = type.name,
            parentCode = parentCode,
        )
        localCodeDataSource.saveCodes(
            codes = response.codes.map {
                CodeLocalEntity(
                    code = it.code,
                    keyword = it.keyword,
                    type = type.name,
                    parentCode = parentCode,
                    cachedAt = now,
                )
            },
        )

        return response
    }
}
