package team.retum.data.repository.student

import team.retum.network.model.request.student.ChangePasswordRequest
import team.retum.network.model.request.student.EditProfileImageRequest
import team.retum.network.model.request.student.ForgottenPasswordRequest
import team.retum.network.model.request.student.PostSignUpRequest
import team.retum.network.model.response.FetchStudentInformationResponse
interface StudentRepository {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )
    suspend fun fetchStudentInformation(): FetchStudentInformationResponse

    suspend fun postSignUp(postSignUpRequest: PostSignUpRequest)

    suspend fun comparePassword(password: String)

    suspend fun resetPassword(forgottenPasswordRequest: ForgottenPasswordRequest)

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest)

    suspend fun editProfileImage(editProfileImageRequest: EditProfileImageRequest)
}
