package team.retum.jobis.local.datasource.code

import team.retum.jobis.local.entity.CodeLocalEntity

interface LocalCodeDataSource {

    suspend fun getCodes(
        type: String,
        parentCode: Long?,
    ): List<CodeLocalEntity>

    suspend fun saveCodes(codes: List<CodeLocalEntity>)

    suspend fun deleteByGroup(
        type: String,
        parentCode: Long?,
    )
}
