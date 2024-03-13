package team.retum.data.repository.bug

import team.retum.network.model.request.bug.ReportBugRequest

interface BugRepository {
    suspend fun reportBug(reportBugRequest: ReportBugRequest)
}
