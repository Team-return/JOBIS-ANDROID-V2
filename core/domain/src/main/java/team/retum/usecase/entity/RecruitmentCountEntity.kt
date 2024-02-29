package team.retum.usecase.entity

import team.retum.network.model.response.FetchRecruitmentPageCountResponse

data class RecruitmentCountEntity(
    val totalPageCount: Long,
)

internal fun FetchRecruitmentPageCountResponse.toRecruitmentCountEntity() = RecruitmentCountEntity(
    totalPageCount = this.totalPageCount,
)
