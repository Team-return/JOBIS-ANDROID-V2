package team.retum.network.datasource.bug

import team.retum.network.api.BugApi
import team.retum.network.model.request.bug.ReportBugRequest
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteBugDataSourceImpl @Inject constructor(
    private val bugApi: BugApi,
) : RemoteBugDataSource {
    override suspend fun reportBug(reportBugRequest: ReportBugRequest) {
        RequestHandler<Unit>().request {
            bugApi.reportBug(reportBugRequest = reportBugRequest)
        }
    }
}
