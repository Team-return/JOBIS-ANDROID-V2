package team.retum.usecase.usecase.bug

import team.retum.common.enums.DevelopmentArea
import team.retum.data.repository.bug.BugRepository
import team.retum.network.model.request.bug.ReportBugRequest
import javax.inject.Inject

class ReportBugUseCase @Inject constructor(
    private val bugRepository: BugRepository,
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        developmentArea: DevelopmentArea,
        attachmentUrls: List<String>,
    ) = runCatching {
        bugRepository.reportBug(
            reportBugRequest = ReportBugRequest(
                title = title,
                content = content,
                developmentArea = developmentArea,
                attachmentUrls = attachmentUrls,
            ),
        )
    }
}
