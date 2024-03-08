package team.retum.data.repository.student

import team.retum.network.model.response.FetchStudentInformationResponse
import team.retum.network.model.request.student.PostSignUpRequest
interface StudentRepository {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )
    suspend fun fetchStudentInformation(): FetchStudentInformationResponse
    suspend fun postSignUp(postSignUpRequest: PostSignUpRequest)
}
