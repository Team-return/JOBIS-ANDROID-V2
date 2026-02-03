package team.retum.usecase.entity.interview

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.interview.FetchInterviewResponse

@Immutable
data class InterviewsEntity(
    val totalCount: Int,
    val interviews: List<InterviewEntity>,
) {
    @Immutable
    data class InterviewEntity(
        val id: Long,
        val interviewType: String,
        val startDate: String,
        val endDate: String,
        val interviewTime: String,
        val companyName: String,
        val location: String,
        val documentNumberId: Long?,
    )
}

internal fun FetchInterviewResponse.toEntity() = InterviewsEntity(
    totalCount = this.totalCount,
    interviews = this.interviews.map { it.toEntity() },
)

private fun FetchInterviewResponse.InterviewResponse.toEntity() = InterviewsEntity.InterviewEntity(
    id = this.id,
    interviewType = this.interviewType,
    startDate = this.startDate,
    endDate = this.endDate,
    interviewTime = this.interviewTime,
    companyName = this.companyName,
    location = this.location,
    documentNumberId = this.documentNumberId,
)