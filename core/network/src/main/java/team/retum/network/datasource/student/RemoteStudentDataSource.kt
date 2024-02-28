package team.retum.network.datasource.student

import team.retum.network.model.response.FetchStudentInformationResponse

interface RemoteStudentDataSource {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )

    suspend fun fetchStudentInformation(): FetchStudentInformationResponse
}
