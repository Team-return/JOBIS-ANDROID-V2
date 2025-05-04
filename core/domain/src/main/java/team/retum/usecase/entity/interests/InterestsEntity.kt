package team.retum.usecase.entity.interests

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.interests.FetchInterestsResponse

@Immutable
data class InterestsEntity(
    val id: Int,
    val studentId: Int,
    val code: Int,
    val keyword: String,
)

internal fun FetchInterestsResponse.toEntity() = InterestsEntity(
    id = this.id,
    studentId = this.studentId,
    code = this.code,
    keyword = this.keyword,
)
