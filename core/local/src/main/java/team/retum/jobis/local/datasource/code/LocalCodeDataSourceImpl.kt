package team.retum.jobis.local.datasource.code

import team.retum.jobis.local.dao.CodeDao
import team.retum.jobis.local.entity.CodeLocalEntity
import javax.inject.Inject

class LocalCodeDataSourceImpl @Inject constructor(
    private val codeDao: CodeDao,
) : LocalCodeDataSource {

    override suspend fun getCodes(
        type: String,
        parentCode: Long?,
    ): List<CodeLocalEntity> = codeDao.getCodes(
        type = type,
        parentCode = parentCode,
    )

    override suspend fun saveCodes(codes: List<CodeLocalEntity>) =
        codeDao.insertAll(codes)

    override suspend fun deleteByGroup(
        type: String,
        parentCode: Long?,
    ) = codeDao.deleteByGroup(
        type = type,
        parentCode = parentCode,
    )
}
