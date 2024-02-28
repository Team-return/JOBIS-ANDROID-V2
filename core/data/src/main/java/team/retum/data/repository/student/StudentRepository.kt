package team.retum.data.repository.student

import team.retum.network.model.response.FetchStudentInformationResponse

interface StudentRepository {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )

    suspend fun fetchStudentInformation(): FetchStudentInformationResponse
}
