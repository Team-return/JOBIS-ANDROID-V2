package team.retum.network.datasource.student

import team.retum.network.model.response.FetchStudentInformationResponse
import team.retum.network.model.request.student.PostSignUpRequest
import team.retum.network.model.response.TokenResponse

interface RemoteStudentDataSource {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )
    suspend fun fetchStudentInformation(): FetchStudentInformationResponse
    suspend fun postSignUp(postSignUpRequest: PostSignUpRequest): TokenResponse
}
