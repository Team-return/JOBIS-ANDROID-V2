package team.retum.usecase.entity.interests

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.interests.FetchInterestsResponse

@Immutable
data class InterestsEntity(
    val studentName: String,
    val interests: List<InterestMajorEntity>,
) {
    data class InterestMajorEntity(
        val id: Int,
        val studentId: Int,
        val code: Int,
        val keyword: String,
    )
}

internal fun FetchInterestsResponse.toEntity() = InterestsEntity(
    studentName = this.studentName,
    interests = this.interests.map { it.toEntity() },
)

private fun FetchInterestsResponse.InterestMajor.toEntity() = InterestsEntity.InterestMajorEntity(
    id = this.id,
    studentId = this.studentId,
    code = this.code,
    keyword = this.keyword,
)
