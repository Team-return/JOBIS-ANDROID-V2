package team.retum.network.datasource.bug

import team.retum.network.model.request.bug.ReportBugRequest

interface RemoteBugDataSource {
    suspend fun reportBug(reportBugRequest: ReportBugRequest)
}
