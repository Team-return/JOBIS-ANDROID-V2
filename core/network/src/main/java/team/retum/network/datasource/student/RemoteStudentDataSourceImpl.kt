package team.retum.network.datasource.student

import team.retum.network.api.StudentApi
import team.retum.network.model.response.FetchStudentInformationResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteStudentDataSourceImpl @Inject constructor(
    private val studentApi: StudentApi,
) : RemoteStudentDataSource {
    override suspend fun checkStudentExists(
        gcn: String,
        name: String,
    ) {
        RequestHandler<Unit>().request {
            studentApi.checkStudentExists(
                gcn = gcn,
                name = name,
            )
        }
    }

    override suspend fun fetchStudentInformation(): FetchStudentInformationResponse {
        return RequestHandler<FetchStudentInformationResponse>().request {
            studentApi.fetchStudentInformation()
        }
    }
}
