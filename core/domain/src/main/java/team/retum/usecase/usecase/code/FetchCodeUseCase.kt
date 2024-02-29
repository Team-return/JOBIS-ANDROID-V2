package team.retum.usecase.usecase.code

import team.retum.common.enums.CodeType
import team.retum.data.repository.code.CodeRepository
import team.retum.usecase.entity.toCodesEntity
import javax.inject.Inject

class FetchCodeUseCase @Inject constructor(
    private val codeRepository: CodeRepository,
) {
    suspend operator fun invoke(
        keyword: String?,
        type: CodeType,
        parentCode: Long?,
    ) = runCatching {
        codeRepository.fetchCodes(
            keyword = keyword,
            type = type,
            parentCode = parentCode,
        ).toCodesEntity()
    }
}

