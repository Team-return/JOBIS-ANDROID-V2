package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.bug.ReportBugRequest

interface BugApi {
    @POST(RequestUrls.Bugs.post)
    suspend fun reportBug(
        @Body reportBugRequest: ReportBugRequest,
    )
}
