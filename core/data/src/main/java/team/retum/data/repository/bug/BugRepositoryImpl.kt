package team.retum.data.repository.bug

import team.retum.network.datasource.bug.RemoteBugDataSource
import team.retum.network.model.request.bug.ReportBugRequest
import javax.inject.Inject

class BugRepositoryImpl @Inject constructor(
    private val remoteBugDataSource: RemoteBugDataSource,
): BugRepository {
    override suspend fun reportBug(reportBugRequest: ReportBugRequest) {
        remoteBugDataSource.reportBug(reportBugRequest = reportBugRequest)
    }
}
