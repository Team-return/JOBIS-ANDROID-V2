package team.retum.data.repository.student

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.datasource.student.RemoteStudentDataSource
import team.retum.network.model.request.student.ChangePasswordRequest
import team.retum.network.model.request.student.EditProfileImageRequest
import team.retum.network.model.request.student.ForgottenPasswordRequest
import team.retum.network.model.request.student.PostSignUpRequest
import team.retum.network.model.response.FetchStudentInformationResponse
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val remoteStudentDataSource: RemoteStudentDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : StudentRepository {
    override suspend fun checkStudentExists(
        gcn: String,
        name: String,
    ) {
        remoteStudentDataSource.checkStudentExists(
            gcn = gcn,
            name = name,
        )
    }

    override suspend fun fetchStudentInformation(): FetchStudentInformationResponse {
        return remoteStudentDataSource.fetchStudentInformation()
    }

    override suspend fun postSignUp(postSignUpRequest: PostSignUpRequest) {
        val response = remoteStudentDataSource.postSignUp(postSignUpRequest = postSignUpRequest)
        localUserDataSource.run {
            saveAccessToken(response.accessToken)
            saveAccessExpiresAt(response.accessExpiresAt)
            saveRefreshToken(response.refreshToken)
            saveRefreshExpiresAt(response.refreshExpiresAt)
        }
    }

    override suspend fun comparePassword(password: String) {
        remoteStudentDataSource.comparePassword(password = password)
    }

    override suspend fun resetPassword(forgottenPasswordRequest: ForgottenPasswordRequest) {
        remoteStudentDataSource.resetPassword(forgottenPasswordRequest = forgottenPasswordRequest)
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        remoteStudentDataSource.changePassword(changePasswordRequest = changePasswordRequest)
    }

    override suspend fun editProfileImage(editProfileImageRequest: EditProfileImageRequest) {
        remoteStudentDataSource.editProfileImage(editProfileImageRequest = editProfileImageRequest)
    }
}
