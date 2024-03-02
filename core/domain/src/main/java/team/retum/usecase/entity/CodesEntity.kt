package team.retum.usecase.entity

import team.retum.network.model.response.FetchCodesResponse

data class CodesEntity(
    val codes: List<CodeEntity>,
) {
    data class CodeEntity(
        val code: Long,
        val keyword: String,
    )
}

internal fun FetchCodesResponse.toCodesEntity() = CodesEntity(
    codes = this.codes.map { it.toEntity() },
)

private fun FetchCodesResponse.CodeResponse.toEntity() = CodesEntity.CodeEntity(
    code = this.code,
    keyword = this.keyword,
)
